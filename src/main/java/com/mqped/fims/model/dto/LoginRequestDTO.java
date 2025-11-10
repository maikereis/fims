package com.mqped.fims.model.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) representing the login request payload.
 * <p>
 * The {@code LoginRequestDTO} is used to capture and validate user credentials
 * during the authentication process. It ensures that both username and password
 * are provided before processing a login attempt.
 * </p>
 *
 * <h2>Usage Example</h2>
 * 
 * <pre>{@code
 * @PostMapping("/auth/login")
 * public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
 *     Authentication authentication = authenticationManager.authenticate(
 *             new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
 *     // Proceed with token generation and response...
 * }
 * }</pre>
 */
public class LoginRequestDTO {

    /**
     * The username used for authentication.
     * <p>
     * This field is mandatory and must not be blank.
     * </p>
     */
    @NotBlank(message = "Username is required")
    private String username;

    /**
     * The password associated with the provided username.
     * <p>
     * This field is mandatory and must not be blank.
     * </p>
     */
    @NotBlank(message = "Password is required")
    private String password;

    // --- Constructors ---

    /** Default constructor. */
    public LoginRequestDTO() {
    }

    /**
     * Constructs a {@code LoginRequestDTO} with specified credentials.
     *
     * @param username the username used for authentication
     * @param password the user's password
     */
    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // --- Getters and Setters ---

    /** @return the username used for authentication. */
    public String getUsername() {
        return username;
    }

    /** @param username sets the username used for authentication. */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return the password used for authentication. */
    public String getPassword() {
        return password;
    }

    /** @param password sets the password used for authentication. */
    public void setPassword(String password) {
        this.password = password;
    }
}
