package com.taylorstieff.heavenhr.repo;

import com.taylorstieff.heavenhr.model.*;
import org.hibernate.TransientPropertyValueException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertNull;
@RunWith(SpringRunner.class)
@DataJpaTest
public class ApplicationRepositoryIntTest {

    @Autowired
    private ApplicationRepository applicationRepository;


    @Before
    public void setup() {
        applicationRepository.deleteAll();
    }

    /**
     * An application needs an offer to exist, as shown with the FK constraints
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void test_createApplication_NotPersistOffer() {
        Offer offer = new OfferBuilder()
                .jobTitle(UUID.randomUUID().toString())
                .startDate(LocalDateTime.now())
                .build();

        Application application = new ApplicationBuilder()
                .relatedOffer(offer)
                .resume(UUID.randomUUID().toString())
                .applicationStatus(ApplicationStatus.INVITED)
                .build();

        applicationRepository.save(application);
    }
}