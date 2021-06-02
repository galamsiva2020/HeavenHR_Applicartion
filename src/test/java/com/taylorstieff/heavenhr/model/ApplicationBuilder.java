package com.taylorstieff.heavenhr.model;

import com.taylorstieff.heavenhr.repo.ApplicationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApplicationBuilder {
    private Application application = new Application();

    public Application build() {
        return application;
    }

    public ApplicationBuilder relatedOffer(Offer offer) {
        application.setRelatedOffer(offer);

        return this;
    }

    public ApplicationBuilder resume(String resume) {
        application.setResume(resume);

        return this;
    }

    public ApplicationBuilder applicationStatus(ApplicationStatus applicationStatus) {
        application.setApplicationStatus(applicationStatus);

        return this;
    }

    public ApplicationBuilder candidate(User user) {
        application.setCandidate(user);

        return this;
    }

    public static List<Application> createApplications(int num, ApplicationRepository applicationRepository, Offer relatedOffer) {
        List<Application> applications = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            User user = UserBuilder.createUser(null);

            applications.add(createApplication(applicationRepository, relatedOffer, user));
        }

        return applications;
    }

    public static Application createApplication(ApplicationRepository applicationRepository, Offer relatedOffer, User candidate) {
        Application application = new ApplicationBuilder()
                .applicationStatus(ApplicationStatus.APPLIED)
                .resume(UUID.randomUUID().toString())
                .candidate(candidate)
                .relatedOffer(relatedOffer)
                .build();

        if (applicationRepository != null) {
            applicationRepository.save(application);
        }

        return application;
    }
}
