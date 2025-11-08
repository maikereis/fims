package com.mqped.fims.model.entity;

import java.time.LocalDateTime;

import com.mqped.fims.model.enums.ServiceOrderStatus;
import com.mqped.fims.model.enums.ServiceOrderType;
import com.mqped.fims.validation.annotation.ChronologicalDates;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "service_orders")
@ChronologicalDates(start = "createdAt", end = "executedAt", allowEqual = false, message = "Execution date must be after creation date.")
public class ServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    @NotNull(message = "Target is required.")
    private Target target;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Status is required.")
    private ServiceOrderStatus status = ServiceOrderStatus.CREATED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Service order type is required.")
    private ServiceOrderType type;

    @NotNull(message = "Creation date is required.")
    @PastOrPresent(message = "Creation date cannot be in the future.")
    private LocalDateTime createdAt;
    private LocalDateTime executedAt;

    // Getters e setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public ServiceOrderStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceOrderStatus status) {
        this.status = status;
    }

    public ServiceOrderType getType() {
        return type;
    }

    public void setType(ServiceOrderType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }

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
