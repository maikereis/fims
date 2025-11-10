package com.mqped.fims.service;

import com.mqped.fims.exceptions.InvalidDataException;
import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.ServiceOrder;
import com.mqped.fims.model.enums.ServiceOrderStatus;
import com.mqped.fims.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service class responsible for managing {@link ServiceOrder} entities.
 * <p>
 * Provides CRUD operations and domain-specific query methods for service
 * orders.
 * This service layer performs input validation, handles repository access, and
 * enforces
 * consistency rules such as required target and type fields.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 * <li>Create, update, delete, and query {@link ServiceOrder} entities</li>
 * <li>Validate service order data integrity before persistence</li>
 * <li>Expose filtering operations by status, target, and creation date
 * ranges</li>
 * </ul>
 *
 * <h3>Exception Handling:</h3>
 * <ul>
 * <li>{@link InvalidDataException} – Thrown when the provided service order is
 * invalid</li>
 * <li>{@link ResourceNotFoundException} – Thrown when the requested service
 * order does not exist</li>
 * </ul>
 *
 * @see com.mqped.fims.model.entity.ServiceOrder
 * @see com.mqped.fims.model.enums.ServiceOrderStatus
 * @see com.mqped.fims.repository.ServiceOrderRepository
 * @see CrudService
 *
 * @since 1.0
 */
@Service
public class ServiceOrderService implements CrudService<ServiceOrder, Integer> {

    private final ServiceOrderRepository repository;

    /**
     * Constructs a new {@code ServiceOrderService} with the provided repository.
     *
     * @param repository the repository for managing {@link ServiceOrder} entities
     */
    public ServiceOrderService(ServiceOrderRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new {@link ServiceOrder} after validation.
     *
     * @param order the service order to create
     * @return the persisted {@link ServiceOrder}
     * @throws InvalidDataException if the order is invalid or missing required data
     */
    @Override
    public ServiceOrder add(ServiceOrder order) {
        validate(order);
        return repository.save(order);
    }

    /**
     * Retrieves all existing service orders.
     *
     * @return a list of all {@link ServiceOrder} records
     */
    @Override
    public List<ServiceOrder> findAll() {
        return repository.findAll();
    }

    /**
     * Finds a {@link ServiceOrder} by its unique identifier.
     *
     * @param id the ID of the service order
     * @return the corresponding {@link ServiceOrder}
     * @throws ResourceNotFoundException if no service order exists with the
     *                                   provided ID
     */
    @Override
    public ServiceOrder findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOrder with id " + id + " not found"));
    }

    /**
     * Updates an existing {@link ServiceOrder} with new information.
     *
     * @param id    the ID of the service order to update
     * @param order the updated service order data
     * @return the updated {@link ServiceOrder}
     * @throws InvalidDataException      if the provided data is invalid
     * @throws ResourceNotFoundException if the service order does not exist
     */
    @Override
    public ServiceOrder update(Integer id, ServiceOrder order) {
        validate(order);

        ServiceOrder existing = findById(id);

        existing.setTarget(order.getTarget());
        existing.setType(order.getType());
        existing.setStatus(order.getStatus());
        existing.setExecutedAt(order.getExecutedAt());

        return repository.save(existing);
    }

    /**
     * Deletes a {@link ServiceOrder} by its ID.
     *
     * @param id the ID of the service order to delete
     * @throws ResourceNotFoundException if the service order does not exist
     */
    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ServiceOrder with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    /**
     * Checks whether a {@link ServiceOrder} with the given ID exists.
     *
     * @param id the ID to check
     * @return {@code true} if the service order exists, otherwise {@code false}
     */
    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    /**
     * Returns the total number of {@link ServiceOrder} records.
     *
     * @return the count of service orders
     */
    @Override
    public long count() {
        return repository.count();
    }

    /**
     * Finds all service orders with the specified status.
     *
     * @param status the status to filter by
     * @return a list of {@link ServiceOrder} objects with the specified status
     */
    public List<ServiceOrder> findByStatus(ServiceOrderStatus status) {
        return repository.findByStatus(status);
    }

    /**
     * Finds all service orders associated with a given target entity.
     *
     * @param targetId the ID of the target
     * @return a list of matching {@link ServiceOrder} objects
     */
    public List<ServiceOrder> findByTargetId(Integer targetId) {
        return repository.findByTargetId(targetId);
    }

    /**
     * Finds service orders where the target's distance from the base is within the
     * specified range.
     *
     * @param min the minimum distance
     * @param max the maximum distance
     * @return a list of {@link ServiceOrder} objects within the distance range
     */
    public List<ServiceOrder> findByTargetDistanceFromBaseBetween(Double min, Double max) {
        return repository.findByTargetDistanceFromBaseBetween(min, max);
    }

    /**
     * Finds service orders whose target matches the given signature exactly.
     *
     * @param signature the target signature to match
     * @return a list of matching {@link ServiceOrder} entities
     */
    public List<ServiceOrder> findByTargetSignature(String signature) {
        return repository.findByTargetSignature(signature);
    }

    /**
     * Finds service orders whose target signature contains a given substring.
     *
     * @param partial the substring to search for
     * @return a list of matching {@link ServiceOrder} entities
     */
    public List<ServiceOrder> findByTargetSignatureContaining(String partial) {
        return repository.findByTargetSignatureContaining(partial);
    }

    /**
     * Finds service orders older than a given number of days, based on their
     * creation timestamp.
     *
     * @param days the number of days to use as a cutoff
     * @return a list of {@link ServiceOrder} entities older than the cutoff
     */
    public List<ServiceOrder> findOlderThanDays(long days) {
        LocalDateTime cutoff = LocalDateTime.now().minus(days, ChronoUnit.DAYS);
        return repository.findOlderThan(cutoff);
    }

    /**
     * Finds service orders created between two timestamps.
     *
     * @param start the start timestamp
     * @param end   the end timestamp
     * @return a list of {@link ServiceOrder} entities created between the given
     *         dates
     */
    public List<ServiceOrder> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return repository.findByCreatedAtBetween(start, end);
    }

    /**
     * Validates the provided {@link ServiceOrder} for required fields and
     * consistency.
     *
     * @param order the service order to validate
     * @throws InvalidDataException if any required field (target, type, etc.) is
     *                              missing
     */
    private void validate(ServiceOrder order) {
        if (order == null) {
            throw new InvalidDataException("ServiceOrder cannot be null");
        }
        if (order.getTarget() == null) {
            throw new InvalidDataException("Target is required");
        }
        if (order.getType() == null) {
            throw new InvalidDataException("ServiceOrder type is required");
        }
    }
}
