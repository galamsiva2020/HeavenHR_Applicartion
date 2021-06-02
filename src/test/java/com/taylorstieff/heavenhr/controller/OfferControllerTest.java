package com.taylorstieff.heavenhr.controller;

import com.taylorstieff.heavenhr.model.Offer;
import com.taylorstieff.heavenhr.model.OfferBuilder;
import com.taylorstieff.heavenhr.model.dto.OfferDto;
import com.taylorstieff.heavenhr.repo.OfferRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = OfferController.class)
public class OfferControllerTest {
    @MockBean
    private OfferRepository offerRepository;

    @Autowired
    private OfferController offerController;

    private Offer mockOffer(Long id) {
        Offer offer = OfferBuilder.createOffer(LocalDateTime.now(), null);
        offer.setId(id);

        return offer;
    }

    private OfferDto mockOfferDto(long id) {
        OfferDto offerDto = new OfferDto();
        offerDto.setJobTitle(UUID.randomUUID().toString());
        offerDto.setStartDate(LocalDateTime.now());

        return offerDto;
    }

    @Test
    public void test_getOffer_OptionalPresent() {
        Offer mockOffer = mockOffer(66L);

        when(offerRepository.findOne(mockOffer.getId())).thenReturn(mockOffer);

        Optional<Offer> actualOffer = offerController.getOffer(mockOffer.getId());

        assertTrue(actualOffer.isPresent());

        verify(offerRepository).findOne(mockOffer.getId());
    }


    @Test
    public void test_getOffer_OptionalNull() {
        Offer mockOffer = mockOffer(66L);

        when(offerRepository.findOne(mockOffer.getId())).thenReturn(null);

        Optional<Offer> actualOffer = offerController.getOffer(mockOffer.getId());

        assertFalse(actualOffer.isPresent());

        verify(offerRepository).findOne(mockOffer.getId());
    }

    @Test
    public void test_getOffers() {
        List<Offer> mockOffers = new ArrayList<>();
        mockOffers.add(mockOffer(66L));
        mockOffers.add(mockOffer(77L));

        Page<Offer> mockPage = new PageImpl<>(mockOffers);

        when(offerRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        Page<Offer> actualPage = offerController.getOffers(0);

        assertEquals(mockOffers, actualPage.getContent());

        verify(offerRepository).findAll(any(Pageable.class));
    }

    @Test
    public void test_saveOfferDto() {
        OfferDto mockOfferDto = new OfferDto();
        mockOfferDto.setStartDate(LocalDateTime.now());
        mockOfferDto.setJobTitle(UUID.randomUUID().toString());

        Offer actualOffer = offerController.saveOfferDto(mockOfferDto);

        verify(offerRepository).save(actualOffer);
    }

}
