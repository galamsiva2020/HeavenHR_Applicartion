package com.taylorstieff.heavenhr.repo;

import com.taylorstieff.heavenhr.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIntTest {

    @Autowired
    private UserRepository userRepository;

    @Test(expected = DataIntegrityViolationException.class)
    public void test_saveInvalid_NullEmail() {
        User user = new User();

        userRepository.save(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void test_saveInvalid_BlankEmail() {
        User user = new User();
        user.setEmail("");

        userRepository.save(user);
    }

    @Test
    public void test_findByUsername() {
        String email = UUID.randomUUID().toString() + "@gmail.com";

        Optional<User> expectNoUser = userRepository.findByUsername(email);
        assertFalse(expectNoUser.isPresent());

        User user = new User();
        user.setEmail(email);
        userRepository.save(user);

        Optional<User> actualUser = userRepository.findByUsername(email);

        assertNotNull("The user should be found in the datbase", actualUser);
        assertEquals(email, actualUser.get().getEmail());
    }
}