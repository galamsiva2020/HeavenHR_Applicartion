package com.taylorstieff.heavenhr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The Offer class represents a job offer for which applicants may apply
 */
@Entity
public class Offer implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 1, max = 255)
    private String jobTitle;

    @NotNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startDate;

    @OneToMany(targetEntity = Application.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Application> applications;

    @Formula("(select count(a.id) from application a where a.related_offer_id = id)")
    private Long numberOfApplicants = 0L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Long getNumberOfApplicants() {
        return numberOfApplicants;
    }

    public List<Application> getApplications() {
        if (applications == null) {
            this.applications = new ArrayList<>();
        }

        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}
