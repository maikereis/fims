package com.mqped.fims.repository;

import com.mqped.fims.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * Extends {@link JpaRepository} to provide built-in CRUD operations,
 * pagination, and query derivation for user authentication and management.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 * <li>Lookup users by unique identifiers (username or email)</li>
 * <li>Validate user existence for registration and login flows</li>
 * <li>Integrate seamlessly with Spring Security’s
 * {@code UserDetailsService}</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * Optional<User> userOpt = userRepository.findByUsername("jdoe");
 * boolean emailInUse = userRepository.existsByEmail("john.doe@example.com");
 * }</pre>
 *
 * @see com.mqped.fims.model.entity.User
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Retrieves a user by their unique username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the user, if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves a user by their unique email address.
     *
     * @param email the user's email address
     * @return an {@link Optional} containing the user, if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with the given username already exists.
     *
     * @param username the username to check
     * @return {@code true} if a user with the given username exists, otherwise
     *         {@code false}
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a user with the given email already exists.
     *
     * @param email the email address to check
     * @return {@code true} if a user with the given email exists, otherwise
     *         {@code false}
     */
    Boolean existsByEmail(String email);
}
