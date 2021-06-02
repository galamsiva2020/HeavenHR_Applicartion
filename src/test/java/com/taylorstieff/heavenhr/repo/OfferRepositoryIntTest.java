package com.taylorstieff.heavenhr.repo;

import com.taylorstieff.heavenhr.model.Application;
import com.taylorstieff.heavenhr.model.ApplicationBuilder;
import com.taylorstieff.heavenhr.model.Offer;
import com.taylorstieff.heavenhr.model.OfferBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OfferRepositoryIntTest {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Test(expected = ConstraintViolationException.class)
    public void test_saveInvalid_JobTitleLength() {
        Offer offer = new OfferBuilder()
                .jobTitle("")
                .build();

        offerRepository.save(offer);
    }

    /**
     * The offer needs to be persisted separately before applications can reference it
     */
    @Test(expected = ConstraintViolationException.class)
    public void test_save_withApplications_ConstraintViolation() {
        Offer offer = new OfferBuilder()
                .jobTitle(UUID.randomUUID().toString())
                .startDate(LocalDateTime.now().minus(5, ChronoUnit.DAYS))
                .withApplications(ApplicationBuilder.createApplications(5, applicationRepository, null))
                .build();

        offerRepository.save(offer);
    }
}