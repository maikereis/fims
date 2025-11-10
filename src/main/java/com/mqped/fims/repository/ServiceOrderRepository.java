package com.mqped.fims.repository;

import com.mqped.fims.model.entity.ServiceOrder;
import com.mqped.fims.model.enums.ServiceOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for managing {@link ServiceOrder} entities.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD, pagination,
 * and sorting functionality, plus several custom query methods
 * for domain-specific filtering and analytics.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 * <li>Query service orders by status, target, and distance constraints</li>
 * <li>Retrieve historical or time-bounded service orders</li>
 * <li>Provide access to data for operational monitoring and cleanup</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * List<ServiceOrder> pending = serviceOrderRepository.findByStatus(ServiceOrderStatus.PENDING);
 * List<ServiceOrder> nearby = serviceOrderRepository.findByTargetDistanceFromBaseLessThan(10.0);
 * List<ServiceOrder> recent = serviceOrderRepository.findByCreatedAtBetween(start, end);
 * }</pre>
 *
 * @see com.mqped.fims.model.entity.ServiceOrder
 * @see com.mqped.fims.model.enums.ServiceOrderStatus
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @since 1.0
 */
@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Integer> {

    /**
     * Retrieves all service orders with the given status.
     */
    List<ServiceOrder> findByStatus(ServiceOrderStatus status);

    /**
     * Finds all service orders targeting a specific entity ID.
     */
    List<ServiceOrder> findByTargetId(Integer targetId);

    /**
     * Finds service orders where the target's distance from base
     * is less than the specified maximum.
     */
    List<ServiceOrder> findByTargetDistanceFromBaseLessThan(Double maxDistance);

    /**
     * Finds service orders where the target's distance from base
     * is greater than the specified minimum.
     */
    List<ServiceOrder> findByTargetDistanceFromBaseGreaterThan(Double minDistance);

    /**
     * Finds service orders where the target's distance from base
     * is between the specified range.
     */
    List<ServiceOrder> findByTargetDistanceFromBaseBetween(Double minDistance, Double maxDistance);

    /**
     * Retrieves service orders matching an exact target signature.
     */
    List<ServiceOrder> findByTargetSignature(String signature);

    /**
     * Retrieves service orders containing a partial match in the target signature.
     */
    List<ServiceOrder> findByTargetSignatureContaining(String partial);

    /**
     * Retrieves all service orders created before the given cutoff date/time.
     * Useful for cleanup or archival.
     */
    @Query("SELECT so FROM ServiceOrder so WHERE so.createdAt <= :cutoff")
    List<ServiceOrder> findOlderThan(@Param("cutoff") LocalDateTime cutoff);

    /**
     * Retrieves all service orders created between two timestamps (inclusive).
     */
    @Query("SELECT so FROM ServiceOrder so WHERE so.createdAt BETWEEN :start AND :end")
    List<ServiceOrder> findByCreatedAtBetween(@Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
