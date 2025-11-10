package com.mqped.fims.model.dto;

import java.util.Set;

/**
 * Data Transfer Object (DTO) representing the response payload for a successful
 * JWT authentication.
 * <p>
 * The {@code JwtResponseDTO} encapsulates the authentication token and
 * user-related data
 * returned to the client after a successful login. This includes the JWT token,
 * token type,
 * user identification details, and associated roles.
 * </p>
 *
 * <h2>Usage Example</h2>
 * 
 * <pre>{@code
 * JwtResponseDTO jwtResponse = new JwtResponseDTO(
 *         token,
 *         user.getId(),
 *         user.getUsername(),
 *         user.getEmail(),
 *         userRoles);
 * return ResponseEntity.ok(jwtResponse);
 * }</pre>
 */
public class JwtResponseDTO {

    /** The JWT token issued to the authenticated user. */
    private String token;

    /** The token type, usually "Bearer" to indicate authorization header format. */
    private String type = "Bearer";

    /** The unique identifier of the authenticated user. */
    private Integer id;

    /** The username of the authenticated user. */
    private String username;

    /** The email address of the authenticated user. */
    private String email;

    /** The set of roles (authorities) granted to the user. */
    private Set<String> roles;

    // --- Constructors ---

    /** Default constructor. */
    public JwtResponseDTO() {
    }

    /**
     * Constructs a {@code JwtResponseDTO} with all required fields.
     *
     * @param token    the JWT token issued to the user
     * @param id       the unique identifier of the user
     * @param username the username of the user
     * @param email    the email of the user
     * @param roles    the roles granted to the user
     */
    public JwtResponseDTO(String token, Integer id, String username, String email, Set<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    // --- Getters and Setters ---

    /** @return the JWT token. */
    public String getToken() {
        return token;
    }

    /** @param token sets the JWT token. */
    public void setToken(String token) {
        this.token = token;
    }

    /** @return the token type (e.g., "Bearer"). */
    public String getType() {
        return type;
    }

    /** @param type sets the token type. */
    public void setType(String type) {
        this.type = type;
    }

    /** @return the unique identifier of the authenticated user. */
    public Integer getId() {
        return id;
    }

    /** @param id sets the unique identifier of the authenticated user. */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the username of the authenticated user. */
    public String getUsername() {
        return username;
    }

    /** @param username sets the username of the authenticated user. */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return the email of the authenticated user. */
    public String getEmail() {
        return email;
    }

    /** @param email sets the email of the authenticated user. */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @return the roles granted to the authenticated user. */
    public Set<String> getRoles() {
        return roles;
    }

    /** @param roles sets the roles granted to the authenticated user. */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
