package com.mqped.fims.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * Data Transfer Object (DTO) representing a user registration request.
 * <p>
 * The {@code SignupRequestDTO} is used to capture user-provided information
 * during account creation, including username, email, password, and roles.
 * Validation annotations ensure basic input correctness before processing.
 * </p>
 *
 * <h2>Validation Summary</h2>
 * <ul>
 * <li><b>username</b> — required, 3 to 50 characters</li>
 * <li><b>email</b> — required, valid format, up to 100 characters</li>
 * <li><b>password</b> — required, 6 to 100 characters</li>
 * <li><b>roles</b> — optional, may contain role names as strings (e.g.,
 * "admin", "user")</li>
 * </ul>
 *
 * <h2>Example Usage</h2>
 * 
 * <pre>{@code
 * SignupRequestDTO signup = new SignupRequestDTO();
 * signup.setUsername("jdoe");
 * signup.setEmail("jdoe@example.com");
 * signup.setPassword("securePass123");
 * signup.setRoles(Set.of("user"));
 * }</pre>
 */
public class SignupRequestDTO {

    /**
     * Desired username for the new account.
     * Must be between 3 and 50 characters.
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    /**
     * User's email address.
     * Must be valid and up to 100 characters.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be at most 100 characters")
    private String email;

    /**
     * User's chosen password.
     * Must be between 6 and 100 characters.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    /**
     * Set of role names assigned to the user.
     * <p>
     * Examples: {@code ["user"]}, {@code ["admin", "moderator"]}.
     * </p>
     * Optional — defaults may be applied by the system.
     */
    private Set<String> roles;

    /** Default no-argument constructor. */
    public SignupRequestDTO() {
    }

    /**
     * Constructs a new {@code SignupRequestDTO} with the given username, email, and
     * password.
     *
     * @param username the desired username
     * @param email    the user's email address
     * @param password the user's password
     */
    public SignupRequestDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /** @return the desired username for the account */
    public String getUsername() {
        return username;
    }

    /** @param username the desired username to set */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return the user's email address */
    public String getEmail() {
        return email;
    }

    /** @param email the email address to set */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @return the user's chosen password */
    public String getPassword() {
        return password;
    }

    /** @param password the password to set */
    public void setPassword(String password) {
        this.password = password;
    }

    /** @return the roles assigned to the user */
    public Set<String> getRoles() {
        return roles;
    }

    /** @param roles the roles to assign to the user */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
