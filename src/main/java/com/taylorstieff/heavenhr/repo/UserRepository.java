package com.taylorstieff.heavenhr.repo;

import com.taylorstieff.heavenhr.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The UserRepository handles the CRUD operations for {@link com.taylorstieff.heavenhr.model.User}
 * objects
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    /**
     * Find a user by a given username. In this case, the username is an alias
     * for an email address.
     * @param username - email address to search for
     * @return optional {@link User}
     */
    @Query("select u from User u where u.email = :username")
    Optional<User> findByUsername(@Param(value = "username") String username);
}
