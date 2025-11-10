package com.mqped.fims.service;

import java.util.List;

/**
 * Generic service interface that defines standard CRUD (Create, Read, Update,
 * Delete)
 * operations for domain entities.
 * <p>
 * This interface serves as a contract for all service-layer components in the
 * application,
 * providing a consistent API for basic persistence operations regardless of
 * entity type.
 * </p>
 *
 * <p>
 * Each concrete service implementation (e.g. {@code ClientService},
 * {@code AddressService},
 * {@code ContractAccountService}) should:
 * <ul>
 * <li>Implement validation and business logic before persistence</li>
 * <li>Handle domain-specific exceptions and consistency checks</li>
 * <li>Delegate actual data access to the appropriate repository</li>
 * </ul>
 * </p>
 *
 * @param <T>  the type of the entity managed by this service
 * @param <ID> the type of the entity’s unique identifier (e.g. {@code Integer},
 *             {@code Long}, {@code UUID})
 *
 * @author
 * @since 1.0
 */
public interface CrudService<T, ID> {

    /**
     * Persists a new entity instance.
     *
     * @param entity the entity to create and save
     * @return the persisted entity
     * @throws com.mqped.fims.exceptions.InvalidDataException
     *                                                        if the provided entity
     *                                                        data is invalid
     */
    T add(T entity);

    /**
     * Retrieves all entities of this type from the repository.
     *
     * @return a list containing all stored entities
     */
    List<T> findAll();

    /**
     * Retrieves an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity
     * @return the entity matching the provided ID
     * @throws com.mqped.fims.exceptions.ResourceNotFoundException
     *                                                             if no entity
     *                                                             exists for the
     *                                                             given ID
     */
    T findById(ID id);

    /**
     * Updates an existing entity with new data.
     * <p>
     * Implementations should retrieve the existing entity, apply changes,
     * validate them, and then persist the updated version.
     * </p>
     *
     * @param id     the unique identifier of the entity to update
     * @param entity the updated entity data
     * @return the updated entity
     * @throws com.mqped.fims.exceptions.InvalidDataException
     *                                                             if the provided
     *                                                             data is invalid
     * @throws com.mqped.fims.exceptions.ResourceNotFoundException
     *                                                             if the entity to
     *                                                             update does not
     *                                                             exist
     */
    T update(ID id, T entity);

    /**
     * Deletes an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity to delete
     * @throws com.mqped.fims.exceptions.ResourceNotFoundException
     *                                                             if the entity
     *                                                             does not exist
     */
    void deleteById(ID id);

    /**
     * Checks whether an entity with the specified ID exists.
     *
     * @param id the identifier to check
     * @return {@code true} if an entity with the given ID exists, {@code false}
     *         otherwise
     */
    boolean existsById(ID id);

    /**
     * Counts the total number of entities managed by this service.
     *
     * @return the total count of entities
     */
    long count();
}
