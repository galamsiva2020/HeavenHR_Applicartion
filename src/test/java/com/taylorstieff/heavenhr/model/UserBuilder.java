package com.taylorstieff.heavenhr.model;

import com.taylorstieff.heavenhr.repo.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserBuilder {
    private User user = new User();

    public User build() {
        return user;
    }

    public UserBuilder email(String email) {
        user.setEmail(email);

        return this;
    }

    public UserBuilder withApplications(List<Application> applications) {
        user.setApplications(applications);

        return this;
    }

    public static User createUser(UserRepository userRepository) {
        User user = new UserBuilder()
                .email(UUID.randomUUID().toString() + "@localhost")
                .build();

        if (userRepository != null) {
            userRepository.save(user);
        }

        return user;
    }
}
