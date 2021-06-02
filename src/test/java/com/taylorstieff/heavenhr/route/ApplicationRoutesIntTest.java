package com.taylorstieff.heavenhr.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taylorstieff.heavenhr.model.*;
import com.taylorstieff.heavenhr.model.dto.ApplicationDto;
import com.taylorstieff.heavenhr.repo.ApplicationRepository;
import com.taylorstieff.heavenhr.repo.OfferRepository;
import org.hamcrest.core.IsAnything;
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
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationRoutesIntTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        applicationRepository.deleteAll();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void test_getApplications_NoValues() throws Exception {
        mockMvc.perform(get("/applications"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    public void test_getApplications_FirstPage() throws Exception {
        Offer offer = OfferBuilder.createOffer(LocalDateTime.now(), offerRepository);
        List<Application> allApplications = ApplicationBuilder
                .createApplications(40, applicationRepository, offer);

        mockMvc.perform(get("/applications"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(IsEqual.equalTo(allApplications.get(0).getId().intValue())))
                .andExpect(jsonPath("$[19].id").value(IsEqual.equalTo(allApplications.get(19).getId().intValue())));
    }

    @Test
    public void test_getApplications_SecondPage() throws Exception {
        Offer offer = OfferBuilder.createOffer(LocalDateTime.now(), offerRepository);
        List<Application> allApplications = ApplicationBuilder
                .createApplications(40, applicationRepository, offer);

        mockMvc.perform(get("/applications?page=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(IsEqual.equalTo(allApplications.get(20).getId().intValue())))
                .andExpect(jsonPath("$[19].id").value(IsEqual.equalTo(allApplications.get(39).getId().intValue())));
    }

    @Test
    public void test_get_NoSuchApplication() throws Exception {
        mockMvc.perform(get("/application/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_get_AnApplication() throws Exception {
        Offer offer = OfferBuilder.createOffer(LocalDateTime.now(), offerRepository);
        List<Application> allApplications = ApplicationBuilder
                .createApplications(2, applicationRepository, offer);

        mockMvc.perform(get("/application/" + allApplications.get(1).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(IsEqual.equalTo(allApplications.get(1).getId().intValue())))
                .andExpect(jsonPath("$.resume").value(IsEqual.equalTo(allApplications.get(1).getResume())))
                .andExpect(jsonPath("$.candidate.email").value(IsEqual.equalTo(allApplications.get(1).getCandidate().getEmail())))
                .andExpect(jsonPath("$.applicationStatus").value(IsEqual.equalTo(allApplications.get(1).getApplicationStatus().toString())));
    }

    @Test
    public void test_post_AnApplication() throws Exception {
        Offer offer = OfferBuilder.createOffer(LocalDateTime.now(), offerRepository);

        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setCandidateEmail(UUID.randomUUID().toString() + "@gmail.com");
        applicationDto.setOfferId(offer.getId());
        applicationDto.setResume(UUID.randomUUID().toString());

        MvcResult response = mockMvc.perform(post("/application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(applicationDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(IsAnything.anything()))
                .andReturn();
    }

    @Test
    public void test_post_AnApplication_OfferNotFound() throws Exception {
        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setCandidateEmail(UUID.randomUUID().toString() + "@gmail.com");
        applicationDto.setOfferId(-1);
        applicationDto.setResume(UUID.randomUUID().toString());

        MvcResult response = mockMvc.perform(post("/application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(applicationDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(IsEqual.equalTo("The offer with the specified ID -1 could not be found")))
                .andReturn();

        System.out.println(response.getResponse().getContentAsString());
    }

    @Test
    public void test_put_UpdateApplicationStatus() throws Exception {
        Offer offer = OfferBuilder.createOffer(LocalDateTime.now(), offerRepository);

        User user = new User();
        user.setEmail(UUID.randomUUID().toString() + "@gmail.com");
        Application application = ApplicationBuilder.createApplication(applicationRepository, offer, user);

        application.setApplicationStatus(ApplicationStatus.HIRED);

        MvcResult response = mockMvc.perform(put("/application/" + application.getId() + "/" + ApplicationStatus.HIRED)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.applicationStatus").value(IsEqual.equalTo("HIRED")))
                .andReturn();
    }

    @Test
    public void test_put_UpdateApplicationStatus_ApplicationNotFound() throws Exception {
        MvcResult response = mockMvc.perform(put("/application/" + -1 + "/" + ApplicationStatus.HIRED)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(IsEqual.equalTo("The application with the specified ID -1 could not be found")))
                .andReturn();
    }
}