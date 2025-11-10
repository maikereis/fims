package com.mqped.fims.repository;

import com.mqped.fims.model.entity.Role;
import com.mqped.fims.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Role} entities.
 * <p>
 * Extends {@link JpaRepository} to provide standard CRUD, pagination,
 * and sorting capabilities for the Role entity.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 * <li>Manage persistence of application roles and their relationships</li>
 * <li>Provide efficient lookup methods by {@link RoleName}</li>
 * <li>Support validation checks such as existence by role name</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * Optional<Role> adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN);
 * boolean exists = roleRepository.existsByName(RoleName.ROLE_USER);
 * }</pre>
 *
 * @see com.mqped.fims.model.entity.Role
 * @see com.mqped.fims.model.enums.RoleName
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @since 1.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Retrieves a role by its enumerated {@link RoleName}.
     *
     * @param name the name of the role (e.g., {@code RoleName.ROLE_ADMIN})
     * @return an {@link Optional} containing the matching role, if found
     */
    Optional<Role> findByName(RoleName name);

    /**
     * Checks whether a role with the given name already exists.
     *
     * @param name the name of the role
     * @return {@code true} if a role with the given name exists, otherwise
     *         {@code false}
     */
    Boolean existsByName(RoleName name);
}
