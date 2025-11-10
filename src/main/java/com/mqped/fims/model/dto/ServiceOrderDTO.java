package com.mqped.fims.model.dto;

import com.mqped.fims.model.entity.ServiceOrder;
import com.mqped.fims.model.enums.ServiceOrderStatus;
import com.mqped.fims.model.enums.ServiceOrderType;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) that represents a summarized view of a
 * {@link ServiceOrder} entity.
 * <p>
 * The {@code ServiceOrderDTO} is used to transfer service order information
 * between
 * layers (e.g., from backend to frontend) without exposing internal entity
 * details.
 * It includes identifiers, timestamps, status, and type information.
 * </p>
 *
 * <h2>Usage Example</h2>
 * 
 * <pre>{@code
 * ServiceOrder serviceOrder = serviceOrderRepository.findById(id).orElseThrow();
 * ServiceOrderDTO dto = ServiceOrderDTO.fromEntity(serviceOrder);
 * return ResponseEntity.ok(dto);
 * }</pre>
 */
public class ServiceOrderDTO {

    /**
     * Unique identifier of the service order.
     */
    private Integer id;

    /**
     * Identifier of the target associated with this service order.
     */
    private Integer targetId;

    /**
     * Current status of the service order.
     * <p>
     * See {@link ServiceOrderStatus} for possible values (e.g., CREATED, EXECUTED,
     * CANCELLED).
     * </p>
     */
    private ServiceOrderStatus status;

    /**
     * Type of the service order.
     * <p>
     * See {@link ServiceOrderType} for possible types (e.g., INSTALLATION,
     * MAINTENANCE, DISCONNECTION).
     * </p>
     */
    private ServiceOrderType type;

    /**
     * Timestamp indicating when the service order was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp indicating when the service order was executed, if applicable.
     */
    private LocalDateTime executedAt;

    // --- Getters and Setters ---

    /** @return the unique identifier of the service order */
    public Integer getId() {
        return id;
    }

    /** @param id the unique identifier to set */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the identifier of the associated target */
    public Integer getTargetId() {
        return targetId;
    }

    /** @param targetId the identifier of the target to set */
    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    /** @return the current status of the service order */
    public ServiceOrderStatus getStatus() {
        return status;
    }

    /** @param status the status to set for this service order */
    public void setStatus(ServiceOrderStatus status) {
        this.status = status;
    }

    /** @return the type of the service order */
    public ServiceOrderType getType() {
        return type;
    }

    /** @param type the type to set for this service order */
    public void setType(ServiceOrderType type) {
        this.type = type;
    }

    /** @return the creation timestamp of the service order */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt the creation timestamp to set */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /** @return the execution timestamp of the service order */
    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    /** @param executedAt the execution timestamp to set */
    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }

    // --- Conversion Methods ---

    /**
     * Converts a {@link ServiceOrder} entity to a {@code ServiceOrderDTO}.
     * <p>
     * This method extracts only relevant fields for API exposure,
     * avoiding lazy-loading of unnecessary associations.
     * </p>
     *
     * @param serviceOrder the {@link ServiceOrder} entity to convert
     * @return a populated {@code ServiceOrderDTO} instance
     */
    public static ServiceOrderDTO fromEntity(ServiceOrder serviceOrder) {
        ServiceOrderDTO dto = new ServiceOrderDTO();
        dto.setId(serviceOrder.getId());
        dto.setStatus(serviceOrder.getStatus());
        dto.setType(serviceOrder.getType());
        dto.setCreatedAt(serviceOrder.getCreatedAt());
        dto.setExecutedAt(serviceOrder.getExecutedAt());

        if (serviceOrder.getTarget() != null) {
            dto.setTargetId(serviceOrder.getTarget().getId());
        }

        return dto;
    }
}
