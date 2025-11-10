package com.mqped.fims.model.entity;

import com.mqped.fims.model.enums.RoleName;

import jakarta.persistence.*;

/**
 * Represents a user role within the system.
 * <p>
 * The {@code Role} entity defines the set of permissions or access levels
 * assigned to users. Each role corresponds to a predefined value in the
 * {@link RoleName} enumeration.
 * </p>
 *
 * <h2>Usage</h2>
 * <p>
 * Roles are typically associated with {@code User} entities to control
 * authentication and authorization within the application.
 * </p>
 *
 * <h2>Database Mapping</h2>
 * <ul>
 * <li>Table name: {@code roles}</li>
 * <li>Primary key: {@code id}</li>
 * <li>Column: {@code name} — unique string representation of the role.</li>
 * </ul>
 *
 * <h2>Example</h2>
 * 
 * <pre>{@code
 * Role adminRole = new Role(RoleName.ROLE_ADMIN);
 * }</pre>
 */
@Entity
@Table(name = "roles")
public class Role {

    /** Primary key identifier for the role. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The unique name of the role, stored as a string.
     * <p>
     * The value must correspond to one of the constants in {@link RoleName}.
     * </p>
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 40, nullable = false, unique = true)
    private RoleName name;

    /** Default constructor required by JPA. */
    public Role() {
    }

    /**
     * Constructs a role with the specified {@link RoleName}.
     *
     * @param name the role name, must not be null
     */
    public Role(RoleName name) {
        this.name = name;
    }

    /** @return the unique identifier of this role. */
    public Integer getId() {
        return id;
    }

    /** @param id sets the unique identifier of this role. */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the {@link RoleName} assigned to this role. */
    public RoleName getName() {
        return name;
    }

    /** @param name sets the {@link RoleName} assigned to this role. */
    public void setName(RoleName name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the role, including its ID and name.
     *
     * @return formatted string with role details
     */
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
