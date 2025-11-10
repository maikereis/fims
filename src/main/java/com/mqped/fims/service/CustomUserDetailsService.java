package com.mqped.fims.service;

import com.mqped.fims.exceptions.AccountDisabledException;
import com.mqped.fims.exceptions.AccountLockedException;
import com.mqped.fims.exceptions.UnauthorizedException;
import com.mqped.fims.model.entity.User;
import com.mqped.fims.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UserDetailsService} that integrates with the
 * application’s
 * domain model and authentication layer.
 * <p>
 * This service is responsible for loading {@link UserDetails} based on a
 * username or email,
 * validating account state (e.g. enabled, locked), and mapping domain roles to
 * Spring Security
 * authorities.
 * </p>
 *
 * <p>
 * It plays a central role in Spring Security’s authentication process, where
 * the returned
 * {@code UserDetails} object is used to verify credentials and assign granted
 * authorities.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 * <li>Retrieve user entities from the persistence layer via
 * {@link UserRepository}</li>
 * <li>Enforce authentication-related constraints (disabled, locked, etc.)</li>
 * <li>Convert domain roles into Spring Security authorities</li>
 * </ul>
 *
 * <h3>Exception Handling:</h3>
 * <ul>
 * <li>{@link UnauthorizedException} – Thrown when a user is not found</li>
 * <li>{@link AccountDisabledException} – Thrown when the user account is
 * disabled</li>
 * <li>{@link AccountLockedException} – Thrown when the user account is
 * locked</li>
 * </ul>
 *
 * @see org.springframework.security.core.userdetails.UserDetailsService
 * @see org.springframework.security.core.userdetails.UserDetails
 * @see com.mqped.fims.model.entity.User
 * @see com.mqped.fims.repository.UserRepository
 *
 * @since 1.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

        private final UserRepository userRepository;

        /**
         * Constructs a new {@code CustomUserDetailsService} with the provided
         * {@link UserRepository}.
         *
         * @param userRepository the repository used to access user data
         */
        public CustomUserDetailsService(UserRepository userRepository) {
                this.userRepository = userRepository;
        }

        /**
         * Loads user details by username for Spring Security authentication.
         * <p>
         * Performs additional domain checks such as:
         * <ul>
         * <li>Whether the account is enabled</li>
         * <li>Whether the account is locked</li>
         * </ul>
         * </p>
         *
         * @param username the username of the user to be loaded
         * @return the fully initialized {@link UserDetails} object
         * @throws UnauthorizedException    if no user with the given username exists
         * @throws AccountDisabledException if the user’s account is disabled
         * @throws AccountLockedException   if the user’s account is locked
         */
        @Override
        @Transactional(readOnly = true)
        public UserDetails loadUserByUsername(String username) {
                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UnauthorizedException(
                                                "User not found with username: " + username));

                // Check if account is enabled
                if (Boolean.FALSE.equals(user.getEnabled())) {
                        throw new AccountDisabledException(
                                        "Account is disabled. Please contact support.");
                }

                // Check if account is locked
                if (Boolean.FALSE.equals(user.getAccountNonLocked())) {
                        throw new AccountLockedException(
                                        "Account is locked. Please contact support.");
                }

                Set<GrantedAuthority> authorities = user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                                .collect(Collectors.toSet());

                return new org.springframework.security.core.userdetails.User(
                                user.getUsername(),
                                user.getPassword(),
                                user.getEnabled(),
                                true, // accountNonExpired
                                true, // credentialsNonExpired
                                user.getAccountNonLocked(),
                                authorities);
        }

        /**
         * Loads user details by email instead of username.
         * <p>
         * This method is functionally equivalent to
         * {@link #loadUserByUsername(String)},
         * but uses the user’s email address for lookup.
         * </p>
         *
         * @param email the email address of the user
         * @return the corresponding {@link UserDetails} object
         * @throws UnauthorizedException    if no user with the given email exists
         * @throws AccountDisabledException if the user’s account is disabled
         * @throws AccountLockedException   if the user’s account is locked
         */
        @Transactional(readOnly = true)
        public UserDetails loadUserByEmail(String email) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new UnauthorizedException(
                                                "User not found with email: " + email));

                if (Boolean.FALSE.equals(user.getEnabled())) {
                        throw new AccountDisabledException(
                                        "Account is disabled. Please contact support.");
                }

                if (Boolean.FALSE.equals(user.getAccountNonLocked())) {
                        throw new AccountLockedException(
                                        "Account is locked. Please contact support.");
                }

                Set<GrantedAuthority> authorities = user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                                .collect(Collectors.toSet());

                return new org.springframework.security.core.userdetails.User(
                                user.getUsername(),
                                user.getPassword(),
                                user.getEnabled(),
                                true,
                                true,
                                user.getAccountNonLocked(),
                                authorities);
        }
}
