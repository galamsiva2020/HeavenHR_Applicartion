package com.taylorstieff.heavenhr.model;

import com.taylorstieff.heavenhr.repo.OfferRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OfferBuilder {
    private Offer offer = new Offer();

    public Offer build() {
        return offer;
    }

    public OfferBuilder jobTitle(String jobTitle) {
        offer.setJobTitle(jobTitle);

        return this;
    }

    public OfferBuilder startDate(LocalDateTime startDate) {
        offer.setStartDate(startDate);

        return this;
    }

    public OfferBuilder withApplications(List<Application> applications) {
        offer.setApplications(applications);

        return this;
    }


    public static Offer createOffer(LocalDateTime startDate, OfferRepository offerRepository) {
        Offer offer = new Offer();
        offer.setJobTitle(UUID.randomUUID().toString());
        offer.setStartDate(startDate);

        if (offerRepository != null) {
            offerRepository.save(offer);
        }

        return offer;
    }

    public static List<Offer> createOffers(int num, OfferRepository offerRepository) {
        List<Offer> offers = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            Offer offer = createOffer(LocalDateTime.now().minus(i, ChronoUnit.DAYS), offerRepository);
            offers.add(offer);
        }

        return offers;
    }
}
