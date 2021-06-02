package com.taylorstieff.heavenhr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An Application represents a job application for a single {@link Offer}
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "application_candidate", columnNames = { "related_offer_id", "candidate_id" }) })
public class Application {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @OneToOne(targetEntity = Offer.class, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @NotNull
    @JsonIgnore
    private Offer relatedOffer;

    @OneToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @NotNull
    private User candidate;

    private String resume;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    public Application() {}

    public Application(Offer offer) {
        relatedOffer = offer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Offer getRelatedOffer() {
        return relatedOffer;
    }

    public void setRelatedOffer(Offer relatedOffer) {
        this.relatedOffer = relatedOffer;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public User getCandidate() {
        return candidate;
    }

    public void setCandidate(User candidate) {
        this.candidate = candidate;
    }
}
