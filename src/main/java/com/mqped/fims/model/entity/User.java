package com.mqped.fims.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an application user within the FIMS system.
 * <p>
 * A {@code User} entity stores authentication and authorization details such as
 * username, password, and assigned {@link Role}s. It also tracks account status
 * flags
 * (enabled/locked) and lifecycle timestamps for auditing and security purposes.
 * </p>
 *
 * <p>
 * <strong>Database table:</strong> {@code users}
 * </p>
 *
 * <p>
 * This entity enforces unique constraints on both {@code username} and
 * {@code email},
 * ensuring each user can be uniquely identified by either.
 * </p>
 *
 * @author MQPED
 * @see Role
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The username used for authentication.
     * <p>
     * Must be between 8 and 50 characters and unique across the system.
     * </p>
     */
    @NotBlank(message = "Username is required")
    @Size(min = 8, max = 50, message = "Username must be between 8 and 50 characters")
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * The user’s email address.
     * <p>
     * Must be unique and formatted as a valid email.
     * </p>
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * The user’s encrypted password.
     * <p>
     * Must contain at least 8 characters.
     * </p>
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(nullable = false)
    private String password;

    /**
     * The set of roles assigned to this user.
     * <p>
     * This defines the permissions available to the user in the system.
     * </p>
     * <p>
     * The relationship is eager-loaded and uses the {@code user_roles} join table.
     * </p>
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * Indicates whether the user account is active and allowed to log in.
     * Defaults to {@code true}.
     */
    @Column(nullable = false)
    private Boolean enabled = true;

    /**
     * Indicates whether the user account is locked.
     * Defaults to {@code true}, meaning the account is not locked.
     */
    @Column(nullable = false)
    private Boolean accountNonLocked = true;

    /**
     * Timestamp of when the user account was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp of the last update to the user record.
     */
    private LocalDateTime updatedAt;

    /**
     * Timestamp of the last successful login.
     */
    private LocalDateTime lastLogin;

    // ---------------------------------------------------
    // Lifecycle Callbacks
    // ---------------------------------------------------

    /**
     * Automatically sets {@link #createdAt} and {@link #updatedAt}
     * to the current timestamp before the entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Automatically updates {@link #updatedAt} timestamp
     * when the entity is modified.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ---------------------------------------------------
    // Constructors
    // ---------------------------------------------------

    /**
     * Default no-args constructor.
     */
    public User() {
    }

    /**
     * Convenience constructor for creating a user with basic fields.
     *
     * @param username the username
     * @param email    the email address
     * @param password the encrypted password
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // ---------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------

    /** @return the user ID */
    public Integer getId() {
        return id;
    }

    /** @param id sets the user ID */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the username */
    public String getUsername() {
        return username;
    }

    /** @param username sets the username */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return the user’s email address */
    public String getEmail() {
        return email;
    }

    /** @param email sets the user’s email address */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @return the encrypted password */
    public String getPassword() {
        return password;
    }

    /** @param password sets the encrypted password */
    public void setPassword(String password) {
        this.password = password;
    }

    /** @return the set of roles assigned to this user */
    public Set<Role> getRoles() {
        return roles;
    }

    /** @param roles sets the user’s roles */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /** @return whether the user account is enabled */
    public Boolean getEnabled() {
        return enabled;
    }

    /** @param enabled sets whether the user account is enabled */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /** @return whether the account is not locked */
    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    /** @param accountNonLocked sets whether the account is locked or unlocked */
    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    /** @return the creation timestamp */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt sets the creation timestamp */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /** @return the last update timestamp */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /** @param updatedAt sets the last update timestamp */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /** @return the timestamp of the last login */
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    /** @param lastLogin sets the timestamp of the last login */
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
