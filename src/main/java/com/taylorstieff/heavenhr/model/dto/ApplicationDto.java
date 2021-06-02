package com.taylorstieff.heavenhr.model.dto;

import com.taylorstieff.heavenhr.model.Application;
import com.taylorstieff.heavenhr.model.ApplicationStatus;
import com.taylorstieff.heavenhr.model.Offer;
import com.taylorstieff.heavenhr.model.User;

/**
 * The ApplicationDto stores information required to persist an {@link Application}
 */
public class ApplicationDto {
    private long offerId;
    private String candidateEmail;
    private String resume;

    public long getOfferId() {
        return offerId;
    }

    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public Application toApplication(Offer relatedOffer, User candidate) {
        Application application = new Application();
        application.setApplicationStatus(ApplicationStatus.APPLIED);
        application.setResume(this.resume);
        application.setCandidate(candidate);
        application.setRelatedOffer(relatedOffer);

        return application;
    }
}
