package com.taylorstieff.heavenhr.controller;

import com.taylorstieff.heavenhr.model.Offer;
import com.taylorstieff.heavenhr.model.dto.OfferDto;
import com.taylorstieff.heavenhr.repo.OfferRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * The OfferController handles any business logic for {@link com.taylorstieff.heavenhr.model.Offer} objects
 */
@Controller
public class OfferController {

    private static final Logger log = Logger.getLogger(OfferController.class);

    @Value("${offers.page.size:20}")
    private int pageSize;

    @Autowired
    private OfferRepository offerRepository;

    /**
     * Retrieve a single {@link Offer} object if found, otherwise an empty {@link Optional}
     * @param id - ID of offer to find
     * @return optional offer
     */
    public Optional<Offer> getOffer(final long id) {
        return Optional.ofNullable(offerRepository.findOne(id));
    }

    /**
     * Retrieve a single page of {@link Offer} objects
     * @param page - page to retrive, 0 indexed
     * @return possibly empty {@link Page} of offers
     */
    public Page<Offer> getOffers(final int page) {
        final Pageable pageRequest = new PageRequest(page, pageSize);
        return offerRepository.findAll(pageRequest);
    }

    /**
     * Takes an {@link OfferDto} converts it to an offer and saves it
     * @param offerDto - to save
     * @return {@link Offer} saved with ID
     */
    @Transactional
    public Offer saveOfferDto(final OfferDto offerDto) {
        final Offer offer = offerDto.toOffer();

        offerRepository.save(offer);

        log.info(String.format("Save offer for %s with new ID %d", offer.getJobTitle(), offer.getId()));

        return offer;
    }

}
