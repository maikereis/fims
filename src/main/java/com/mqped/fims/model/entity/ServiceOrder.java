package com.mqped.fims.model.entity;

import java.time.LocalDateTime;

import com.mqped.fims.model.enums.ServiceOrderStatus;
import com.mqped.fims.model.enums.ServiceOrderType;
import com.mqped.fims.validation.annotation.ChronologicalDates;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

/**
 * Represents a service order associated with a specific {@link Target}.
 * <p>
 * The {@code ServiceOrder} entity tracks the lifecycle of operations
 * or maintenance tasks performed on a target (e.g., installations or accounts),
 * including its type, status, and execution timestamps.
 * </p>
 *
 * <h2>Validation Rules</h2>
 * <ul>
 * <li>{@code createdAt} must not be in the future.</li>
 * <li>{@code executedAt} must be after {@code createdAt} (enforced by
 * {@link ChronologicalDates}).</li>
 * <li>{@code target}, {@code status}, and {@code type} are required
 * fields.</li>
 * </ul>
 *
 * <h2>Database Mapping</h2>
 * <ul>
 * <li>Table name: {@code service_orders}</li>
 * <li>Primary key: {@code id}</li>
 * <li>Foreign key: {@code target_id}</li>
 * </ul>
 *
 * <h2>Example</h2>
 * 
 * <pre>{@code
 * ServiceOrder order = new ServiceOrder();
 * order.setTarget(target);
 * order.setType(ServiceOrderType.MAINTENANCE);
 * order.setStatus(ServiceOrderStatus.CREATED);
 * order.setCreatedAt(LocalDateTime.now());
 * }</pre>
 */
@Entity
@Table(name = "service_orders")
@ChronologicalDates(start = "createdAt", end = "executedAt", allowEqual = false, message = "Execution date must be after creation date.")
public class ServiceOrder {

    /** Unique identifier for the service order. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The target entity associated with this service order.
     * <p>
     * Represents the subject of the service order (e.g., a client installation or
     * contract).
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    @NotNull(message = "Target is required.")
    private Target target;

    /**
     * The current status of the service order.
     * <p>
     * Defaults to {@link ServiceOrderStatus#CREATED}.
     * </p>
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Status is required.")
    private ServiceOrderStatus status = ServiceOrderStatus.CREATED;

    /**
     * The type of service order to be executed.
     * <p>
     * Defines the purpose or nature of the order (e.g., installation, maintenance).
     * </p>
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Service order type is required.")
    private ServiceOrderType type;

    /** The date and time when the service order was created. */
    @NotNull(message = "Creation date is required.")
    @PastOrPresent(message = "Creation date cannot be in the future.")
    private LocalDateTime createdAt;

    /** The date and time when the service order was executed. */
    private LocalDateTime executedAt;

    /** @return the unique identifier of the service order. */
    public Integer getId() {
        return id;
    }

    /** @param id sets the unique identifier of the service order. */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the {@link Target} entity associated with this service order. */
    public Target getTarget() {
        return target;
    }

    /** @param target sets the target associated with this service order. */
    public void setTarget(Target target) {
        this.target = target;
    }

    /** @return the current {@link ServiceOrderStatus} of the service order. */
    public ServiceOrderStatus getStatus() {
        return status;
    }

    /** @param status sets the {@link ServiceOrderStatus} of the service order. */
    public void setStatus(ServiceOrderStatus status) {
        this.status = status;
    }

    /**
     * @return the {@link ServiceOrderType} representing the type of service order.
     */
    public ServiceOrderType getType() {
        return type;
    }

    /** @param type sets the {@link ServiceOrderType} of the service order. */
    public void setType(ServiceOrderType type) {
        this.type = type;
    }

    /** @return the timestamp when the service order was created. */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt sets the creation timestamp of the service order. */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the timestamp when the service order was executed, or null if not yet
     *         executed.
     */
    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    /** @param executedAt sets the execution timestamp of the service order. */
    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }

    /**
     * Returns a string representation of the service order including its key
     * attributes.
     *
     * @return formatted string with service order details
     */
    @Override
    public String toString() {
        return "ServiceOrder{" +
                "id=" + id +
                ", target=" + (target != null ? target.getId() : "null") +
                ", status=" + status +
                ", type=" + type +
                ", createdAt=" + createdAt +
                ", executedAt=" + executedAt +
                '}';
    }
}
