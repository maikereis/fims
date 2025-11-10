package com.mqped.fims.model.enums;

/**
 * Enumeration representing the different roles available within the system.
 * <p>
 * The {@code RoleName} enum defines application-level authorization roles
 * that determine user permissions and access control across secured endpoints.
 * </p>
 *
 * <h2>Usage</h2>
 * These roles are typically used in conjunction with Spring Security annotations
 * such as {@code @PreAuthorize}, {@code @Secured}, or role-based JWT claims.
 *
 * <h2>Defined Roles</h2>
 * <ul>
 *   <li>{@link #ROLE_USER} — Standard user role with basic permissions.</li>
 *   <li>{@link #ROLE_ADMIN} — Administrative role with full access to system operations.</li>
 *   <li>{@link #ROLE_MODERATOR} — Intermediate role with permissions to manage or review user content.</li>
 * </ul>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * if (user.getRoles().contains(RoleName.ROLE_ADMIN)) {
 *     // perform administrative task
 * }
 * }</pre>
 */
public enum RoleName {

    /** Standard user role with limited permissions. */
    ROLE_USER,

    /** System administrator role with unrestricted privileges. */
    ROLE_ADMIN,

    /** Moderator role with extended permissions for review and management. */
    ROLE_MODERATOR
}
