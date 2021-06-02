package com.taylorstieff.heavenhr.controller;

import com.taylorstieff.heavenhr.exception.ApplicationNotFoundException;
import com.taylorstieff.heavenhr.exception.OfferNotFoundException;
import com.taylorstieff.heavenhr.model.Application;
import com.taylorstieff.heavenhr.model.ApplicationStatus;
import com.taylorstieff.heavenhr.model.Offer;
import com.taylorstieff.heavenhr.model.User;
import com.taylorstieff.heavenhr.model.dto.ApplicationDto;
import com.taylorstieff.heavenhr.repo.ApplicationRepository;
import com.taylorstieff.heavenhr.repo.OfferRepository;
import com.taylorstieff.heavenhr.repo.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The OfferController handles any business logic for {@link Offer} objects
 */
@Controller
public class ApplicationController {

    private static final Logger log = Logger.getLogger(ApplicationController.class);

    @Value("${offers.page.size:20}")
    private int pageSize;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationStatusEventController applicationStatusEventController;

    /**
     * Retrieve a single possibly null {@link} Application
     * @param id - application ID
     * @return optional {@link Application}
     */
    public Optional<Application> getApplication(final long id) {
        log.info(String.format("Retrieve application for ID %d", id));

        return Optional.ofNullable(applicationRepository.findOne(id));
    }

    /**
     * Retrieve a {@link Page} of {@link Application} objects
     * @param page - page number to retrieve, 0 indexed
     * @return page of possibly empty applications
     */
    public Page<Application> getApplications(final int page) {
        log.info(String.format("Retrieve applications for page %d", page));

        final Pageable pageRequest = new PageRequest(page, pageSize);
        return applicationRepository.findAll(pageRequest);
    }

    /**
     * Convert an {@link ApplicationDto} to an {@link Application}, find the corresponding
     * {@link Offer} and create a new {@link User} or use an existing one
     * @param applicationDto - Application details to persist
     * @return saved application instance
     * @throws OfferNotFoundException - When the referenced {@link ApplicationDto#getOfferId()}
     * does not exist
     */
    @Transactional
    public Application saveApplicationDto(final ApplicationDto applicationDto) throws OfferNotFoundException {
        final Offer offer = offerRepository.findOne(applicationDto.getOfferId());
        if (offer == null) {
            log.warn(String.format("Could not find offer ID %d when saving application", applicationDto.getOfferId()));
            throw new OfferNotFoundException(applicationDto.getOfferId());
        }

        Optional<User> userOptional = userRepository.findByUsername(applicationDto.getCandidateEmail());
        User user;
        if (!userOptional.isPresent()) {
            log.info(String.format("No existing user found for email %s, creating new user", applicationDto.getCandidateEmail()));

            user = new User();
            user.setEmail(applicationDto.getCandidateEmail());
        } else {
            user = userOptional.get();
        }

        final Application application = applicationDto.toApplication(offer, user);

        applicationRepository.save(application);

        log.info(String.format("Save application for %s with new ID %d", user.getEmail(), application.getId()));

        return application;
    }

    /**
     * Updates the {@link Application#getApplicationStatus()} of an application and triggers
     * the status change handler if the status has truly changed
     * @param id - application ID
     * @param newApplicationStatus - Status to change to
     * @return updated application
     * @throws ApplicationNotFoundException - When the referenced {@link Application#getId()}
     * does not exist
     */
    @Transactional
    public Application updateApplicationStatus(final long id, final ApplicationStatus newApplicationStatus)
            throws ApplicationNotFoundException {
        Application application = applicationRepository.findOne(id);
        if (application == null) {
            log.warn(String.format("Could not find application ID %d when updating application status", id));
            throw new ApplicationNotFoundException(id);
        }

        final ApplicationStatus prevStatus = application.getApplicationStatus();
        application.setApplicationStatus(newApplicationStatus);

        applicationRepository.save(application);

        // Hook for application status changes
        if (newApplicationStatus != prevStatus) {
            log.info(String.format("Application ID %d saved with differing application status", application.getId()));

            applicationStatusEventController
                    .handleApplicationStatusChange(application, newApplicationStatus, prevStatus);
        }

        return application;
    }

    /**
     * Retrieve all of the {@link Application} objects for a specific {@link Offer}
     * @param offerId - ID of offer to search applications for
     * @param page - page number to retrieve, 0 indexed
     * @return {@link Page} of {@link Application} with referenced {@link Offer}
     * @throws OfferNotFoundException - When the provided offer ID does not exist
     */
    public Page<Application> getApplicationsForOffer(long offerId, int page) throws OfferNotFoundException {
        Offer offer = offerRepository.findOne(offerId);
        if (offer == null) {
            log.warn(String.format("Could not find offer ID %d when saving application", offerId));
            throw new OfferNotFoundException(offerId);
        }

        Pageable pageRequest = new PageRequest(page, pageSize);
        Page<Application> applications = applicationRepository.findApplicationsByRelatedOffer(offer, pageRequest);

        return applications;
    }

}
