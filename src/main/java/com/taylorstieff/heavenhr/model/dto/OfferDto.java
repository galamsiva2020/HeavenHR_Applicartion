package com.taylorstieff.heavenhr.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.taylorstieff.heavenhr.model.Offer;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OfferDto implements Serializable {

    private String jobTitle;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startDate;

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

    public Offer toOffer() {
        Offer offer = new Offer();

        offer.setJobTitle(this.jobTitle);
        offer.setStartDate(startDate);

        return offer;
    }
}
