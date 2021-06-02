package com.taylorstieff.heavenhr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 3, max = 320)
    private String email;

    @OneToMany(targetEntity = Application.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Application> applications;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
