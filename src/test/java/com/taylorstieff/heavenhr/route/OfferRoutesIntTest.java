package com.taylorstieff.heavenhr.route;

import com.taylorstieff.heavenhr.model.Application;
import com.taylorstieff.heavenhr.model.ApplicationBuilder;
import com.taylorstieff.heavenhr.model.Offer;
import com.taylorstieff.heavenhr.model.OfferBuilder;
import com.taylorstieff.heavenhr.repo.ApplicationRepository;
import com.taylorstieff.heavenhr.repo.OfferRepository;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OfferRoutesIntTest {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        // Order matters because of cascading operation
        applicationRepository.deleteAll();
        offerRepository.deleteAll();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }


    @Test
    public void test_getOffers_NoValues() throws Exception {
        mockMvc.perform(get("/offers"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    public void test_getOffers_FirstPage() throws Exception {
        List<Offer> allOffers = OfferBuilder.createOffers(40, offerRepository);

        mockMvc.perform(get("/offers"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(IsEqual.equalTo(allOffers.get(0).getId().intValue())))
                .andExpect(jsonPath("$[19].id").value(IsEqual.equalTo(allOffers.get(19).getId().intValue())));
    }

    @Test
    public void test_getOffers_SecondPage() throws Exception {
        List<Offer> allOffers = OfferBuilder.createOffers(40, offerRepository);

        mockMvc.perform(get("/offers?page=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(IsEqual.equalTo(allOffers.get(20).getId().intValue())))
                .andExpect(jsonPath("$[19].id").value(IsEqual.equalTo(allOffers.get(39).getId().intValue())));
    }

    @Test
    public void test_get_NoSuchOffer() throws Exception {
        mockMvc.perform(get("/offer/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_get_AnOffer() throws Exception {
        List<Offer> allOffers = OfferBuilder.createOffers(2, offerRepository);

        mockMvc.perform(get("/offer/" + allOffers.get(1).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(IsEqual.equalTo(allOffers.get(1).getId().intValue())));
    }

    @Test
    public void test_get_ApplicationsForOffer_FirstPage() throws Exception {
        Offer offer = OfferBuilder.createOffer(LocalDateTime.now(), offerRepository);
        List<Application> applications = ApplicationBuilder.createApplications(20, applicationRepository, offer);

        MvcResult response = mockMvc.perform(get("/offer/" + offer.getId() + "/applications"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(IsEqual.equalTo(applications.get(0).getId().intValue())))
                .andExpect(jsonPath("$[19].id").value(IsEqual.equalTo(applications.get(19).getId().intValue())))
                .andReturn();
    }
}