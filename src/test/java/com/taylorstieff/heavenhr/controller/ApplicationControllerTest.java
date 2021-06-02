package com.taylorstieff.heavenhr.controller;

import com.taylorstieff.heavenhr.exception.ApplicationNotFoundException;
import com.taylorstieff.heavenhr.exception.OfferNotFoundException;
import com.taylorstieff.heavenhr.model.*;
import com.taylorstieff.heavenhr.model.dto.ApplicationDto;
import com.taylorstieff.heavenhr.repo.ApplicationRepository;
import com.taylorstieff.heavenhr.repo.OfferRepository;
import com.taylorstieff.heavenhr.repo.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationController.class)
public class ApplicationControllerTest {
    @MockBean
    private ApplicationRepository applicationRepository;

    @MockBean
    private OfferRepository offerRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ApplicationStatusEventController applicationStatusEventController;

    @Autowired
    private ApplicationController applicationController;

    private Application mockApplication(ApplicationStatus originalStatus, long applicationId) {
        Application mockApplication = ApplicationBuilder.createApplication(null,
                OfferBuilder.createOffer(LocalDateTime.now(), null),
                UserBuilder.createUser(null));

        mockApplication.setApplicationStatus(originalStatus);
        mockApplication.setId(66L);
        mockApplication.getRelatedOffer().setId(55L);
        mockApplication.getCandidate().setId(44L);
        return mockApplication;
    }

    private ApplicationDto mockApplicationDto(long id) {
        ApplicationDto mockApplicationDto = new ApplicationDto();
        mockApplicationDto.setResume(UUID.randomUUID().toString());
        mockApplicationDto.setCandidateEmail(UUID.randomUUID().toString());
        mockApplicationDto.setOfferId(id);
        return mockApplicationDto;
    }

    @Test
    public void test_updateApplicationStatus_DifferentStatus() throws ApplicationNotFoundException {
        ApplicationStatus originalStatus = ApplicationStatus.APPLIED;
        ApplicationStatus newStatus = ApplicationStatus.HIRED;

        Application mockApplication = mockApplication(originalStatus, 66);

        when(applicationRepository.findOne(anyLong())).thenReturn(mockApplication);

        Application updatedApplication = applicationController.updateApplicationStatus(mockApplication.getId(), newStatus);

        verify(applicationRepository).save(updatedApplication);
        verify(applicationStatusEventController).handleApplicationStatusChange(mockApplication, newStatus, originalStatus);
    }

    @Test
    public void test_updateApplicationStatus_SameStatus() throws ApplicationNotFoundException {
        ApplicationStatus originalStatus = ApplicationStatus.APPLIED;
        ApplicationStatus newStatus = ApplicationStatus.APPLIED;

        Application mockApplication = mockApplication(originalStatus, 66);

        when(applicationRepository.findOne(anyLong())).thenReturn(mockApplication);

        Application updatedApplication = applicationController.updateApplicationStatus(mockApplication.getId(), newStatus);

        verify(applicationRepository).save(updatedApplication);
        verify(applicationStatusEventController, never()).handleApplicationStatusChange(mockApplication, newStatus, originalStatus);
    }

    @Test
    public void test_getApplication_OptionalPresent() {
        Application mockApplication = mockApplication(ApplicationStatus.APPLIED, 66);

        when(applicationRepository.findOne(mockApplication.getId())).thenReturn(mockApplication);

        Optional<Application> actualApplication = applicationController.getApplication(mockApplication.getId());

        assertTrue(actualApplication.isPresent());

        verify(applicationRepository).findOne(mockApplication.getId());
    }


    @Test
    public void test_getApplication_OptionalNull() {
        Application mockApplication = mockApplication(ApplicationStatus.APPLIED, 66);

        when(applicationRepository.findOne(mockApplication.getId())).thenReturn(null);

        Optional<Application> actualApplication = applicationController.getApplication(mockApplication.getId());

        assertFalse(actualApplication.isPresent());

        verify(applicationRepository).findOne(mockApplication.getId());
    }

    @Test
    public void test_getApplications() {
        List<Application> mockApplications = new ArrayList<>();
        mockApplications.add(mockApplication(ApplicationStatus.APPLIED, 66));
        mockApplications.add(mockApplication(ApplicationStatus.HIRED, 67));

        Page<Application> mockPage = new PageImpl<Application>(mockApplications);

        when(applicationRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        Page<Application> actualPage = applicationController.getApplications(0);

        assertEquals(mockApplications, actualPage.getContent());

        verify(applicationRepository).findAll(any(Pageable.class));
    }

    @Test(expected = OfferNotFoundException.class)
    public void test_saveApplicationDto_InvalidOffer() throws OfferNotFoundException {
        ApplicationDto mockApplicationDto = mockApplicationDto(-1);

        when(offerRepository.findOne(mockApplicationDto.getOfferId())).thenReturn(null);

        applicationController.saveApplicationDto(mockApplicationDto);
    }


    @Test
    public void test_saveApplicationDto_NewUser() throws OfferNotFoundException {
        ApplicationDto mockApplicationDto = mockApplicationDto(99);
        Offer mockOffer = OfferBuilder.createOffer(LocalDateTime.now(), null);
        mockOffer.setId(99L);

        when(offerRepository.findOne(mockApplicationDto.getOfferId())).thenReturn(mockOffer);
        when(userRepository.findByUsername(mockApplicationDto.getCandidateEmail())).thenReturn(Optional.empty());

        Application savedApplication = applicationController.saveApplicationDto(mockApplicationDto);

        verify(offerRepository).findOne(mockApplicationDto.getOfferId());
        verify(userRepository).findByUsername(mockApplicationDto.getCandidateEmail());
        verify(applicationRepository).save(savedApplication);
    }

    @Test
    public void test_saveApplicationDto_ExistingUser() throws OfferNotFoundException {
        ApplicationDto mockApplicationDto = mockApplicationDto(99);
        Offer mockOffer = OfferBuilder.createOffer(LocalDateTime.now(), null);
        mockOffer.setId(99L);

        User mockExistingUser = new User();
        mockExistingUser.setEmail(mockApplicationDto.getCandidateEmail());
        mockExistingUser.setId(222L);

        when(offerRepository.findOne(mockApplicationDto.getOfferId())).thenReturn(mockOffer);
        when(userRepository.findByUsername(mockApplicationDto.getCandidateEmail())).thenReturn(Optional.of(mockExistingUser));

        Application savedApplication = applicationController.saveApplicationDto(mockApplicationDto);

        verify(offerRepository).findOne(mockApplicationDto.getOfferId());
        verify(userRepository).findByUsername(mockExistingUser.getEmail());
        verify(applicationRepository).save(savedApplication);
    }

    @Test
    public void test_getApplicationsForOffer() throws OfferNotFoundException {
        Offer mockOffer = new Offer();
        mockOffer.setId(3344L);

        List<Application> mockApplications = new ArrayList<>();
        mockApplications.add(mockApplication(ApplicationStatus.APPLIED, 66));
        mockApplications.add(mockApplication(ApplicationStatus.HIRED, 67));

        Page<Application> mockPage = new PageImpl<>(mockApplications);

        when(offerRepository.findOne(mockOffer.getId())).thenReturn(mockOffer);
        when(applicationRepository.findApplicationsByRelatedOffer(mockOffer, new PageRequest(0, 20))).thenReturn(mockPage);

        Page<Application> actualPage = applicationController.getApplicationsForOffer(mockOffer.getId(), 0);

        assertEquals(mockApplications, actualPage.getContent());

        verify(offerRepository).findOne(mockOffer.getId());
        verify(applicationRepository).findApplicationsByRelatedOffer(mockOffer, new PageRequest(0, 20));
    }

    @Test(expected = OfferNotFoundException.class)
    public void test_getApplicationsForOffer_NoOffer() throws OfferNotFoundException {
        when(offerRepository.findOne(anyLong())).thenReturn(null);
        Page<Application> actualPage = applicationController.getApplicationsForOffer(3344L, 0);

    }
}
