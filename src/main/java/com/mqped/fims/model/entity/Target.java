package com.mqped.fims.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.mqped.fims.model.enums.TargetType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;

/**
 * Represents a target entity within the FIMS system.
 * <p>
 * A {@code Target} is associated with a specific {@link ContractAccount} and
 * contains
 * metadata used for performance monitoring, distance calculations, and
 * operational tracking.
 * Each target can have multiple {@link ServiceOrder}s associated with it.
 * </p>
 *
 * <p>
 * Validation constraints ensure all numerical values are non-negative and
 * creation dates
 * are not set in the future.
 * </p>
 *
 * <p>
 * <strong>Database table:</strong> {@code targets}
 * </p>
 *
 * @author MQPED
 * @see ContractAccount
 * @see ServiceOrder
 * @see TargetType
 */
@Entity
@Table(name = "targets")
public class Target {

    /**
     * Unique identifier for the target.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The contract account associated with this target.
     * <p>
     * This relationship is mandatory and defines the owner of the target.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_account_id", nullable = false)
    @NotNull(message = "Contract account is required.")
    private ContractAccount contractAccount;

    /**
     * Defines the type of target (e.g., RESIDENTIAL, COMMERCIAL).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Target type is required.")
    private TargetType type;

    /**
     * Unique textual signature identifying the target in external systems.
     */
    @NotBlank(message = "Signature is required.")
    private String signature;

    /**
     * The calculated performance score of the target.
     * May be updated as part of business logic.
     */
    @Min(value = 0)
    private Double score;

    /**
     * Expected consumption or performance metric (CNR) for this target.
     */
    @NotNull(message = "Expected CNR is required.")
    @Min(value = 0)
    private Double expectedCNR;

    /**
     * Expected ticket or revenue value for this target.
     */
    @NotNull(message = "Expected ticket is required.")
    @Min(value = 0)
    private Double expectedTicket;

    /**
     * Distance from the operational base, used in logistics calculations.
     */
    @NotNull(message = "Distance from base is required.")
    @Min(value = 0)
    private Double distanceFromBase;

    /**
     * Date and time when this target was created.
     * Must not be in the future.
     */
    @NotNull(message = "Creation date is required.")
    @PastOrPresent(message = "Creation date cannot be in the future.")
    private LocalDateTime createdAt;

    /**
     * Indicates whether this target is active.
     * Defaults to {@code true}.
     */
    private Boolean active = true;

    /**
     * Automatically sets {@link #createdAt} to the current time if not already
     * defined.
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    /**
     * The list of service orders associated with this target.
     * <p>
     * All service orders will be cascaded and removed when the target is deleted.
     * </p>
     */
    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceOrder> serviceOrders;

    // ---------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------

    /** @return the unique identifier of the target */
    public Integer getId() {
        return id;
    }

    /** @param id sets the unique identifier of the target */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the associated {@link ContractAccount} */
    public ContractAccount getContractAccount() {
        return contractAccount;
    }

    /** @param contractAccount sets the associated {@link ContractAccount} */
    public void setContractAccount(ContractAccount contractAccount) {
        this.contractAccount = contractAccount;
    }

    /** @return the target type */
    public TargetType getType() {
        return type;
    }

    /** @param type sets the target type */
    public void setType(TargetType type) {
        this.type = type;
    }

    /** @return the unique target signature */
    public String getSignature() {
        return signature;
    }

    /** @param signature sets the unique target signature */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /** @return the target score */
    public Double getScore() {
        return score;
    }

    /** @param score sets the target score */
    public void setScore(Double score) {
        this.score = score;
    }

    /** @return the expected CNR value */
    public Double getExpectedCNR() {
        return expectedCNR;
    }

    /** @param expectedCNR sets the expected CNR value */
    public void setExpectedCNR(Double expectedCNR) {
        this.expectedCNR = expectedCNR;
    }

    /** @return the expected ticket value */
    public Double getExpectedTicket() {
        return expectedTicket;
    }

    /** @param expectedTicket sets the expected ticket value */
    public void setExpectedTicket(Double expectedTicket) {
        this.expectedTicket = expectedTicket;
    }

    /** @return the distance from the operational base */
    public Double getDistanceFromBase() {
        return distanceFromBase;
    }

    /** @param distanceFromBase sets the distance from the operational base */
    public void setDistanceFromBase(Double distanceFromBase) {
        this.distanceFromBase = distanceFromBase;
    }

    /** @return the creation timestamp */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt sets the creation timestamp */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /** @return whether the target is active */
    public Boolean getActive() {
        return active;
    }

    /** @param active sets whether the target is active */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /** @return the list of {@link ServiceOrder}s linked to this target */
    public List<ServiceOrder> getServiceOrders() {
        return serviceOrders;
    }

    /**
     * @param serviceOrders sets the list of {@link ServiceOrder}s linked to this
     *                      target
     */
    public void setServiceOrders(List<ServiceOrder> serviceOrders) {
        this.serviceOrders = serviceOrders;
    }

    @Override
    public String toString() {
        return "Target{" +
                "id=" + id +
                ", contractAccount=" + (contractAccount != null ? contractAccount.getId() : "null") +
                ", type=" + type +
                ", expectedCNR=" + expectedCNR +
                ", expectedTicket=" + expectedTicket +
                ", distanceFromBase=" + distanceFromBase +
                ", active=" + active +
                '}';
    }
}
