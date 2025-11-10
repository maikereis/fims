package com.mqped.fims.model.entity;

import java.time.LocalDateTime;

import com.mqped.fims.model.enums.StatusType;
import com.mqped.fims.validation.annotation.ChronologicalDates;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

/**
 * Represents a contractual relationship between a {@link Client} and an
 * {@link Installation}.
 * <p>
 * A {@code ContractAccount} defines a service account that links a client to a
 * specific
 * installation, tracking its lifecycle, status transitions, and relevant
 * timestamps.
 * </p>
 *
 * <p>
 * <strong>Database table:</strong> {@code contract_accounts}
 * </p>
 *
 * <p>
 * The class uses {@link ChronologicalDates} validations to ensure the temporal
 * consistency
 * between creation, deletion, and status dates.
 * </p>
 *
 * <ul>
 * <li>{@code createdAt} ≤ {@code deletedAt}</li>
 * <li>{@code createdAt} ≤ {@code statusStart} ≤ {@code statusEnd}</li>
 * </ul>
 *
 * @author MQPED
 * @see Client
 * @see Installation
 */
@Entity
@Table(name = "contract_accounts")
@ChronologicalDates(start = "createdAt", end = "deletedAt", allowEqual = true, message = "Deleted date must be after creation date.")
@ChronologicalDates(start = "statusStart", end = "statusEnd", allowEqual = true, message = "Status end date must be after status start date.")
@ChronologicalDates(start = "createdAt", end = "statusStart", allowEqual = true, message = "Status start date must be after creation date.")
public class ContractAccount {

    /**
     * Unique identifier for the contract account.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Unique account number assigned to the contract.
     */
    private String accountNumber;

    /**
     * The client associated with this contract account.
     * <p>
     * A client may hold multiple contract accounts.
     * </p>
     */
    @ManyToOne(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = "Client is required.")
    private Client client;

    /**
     * The installation linked to this contract account.
     * <p>
     * Represents the physical or logical location where the service is provided.
     * </p>
     */
    @ManyToOne(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinColumn(name = "installation_id", nullable = false)
    @NotNull(message = "Installation is required.")
    private Installation installation;

    /**
     * Date and time when the contract account was created.
     * <p>
     * Must not be set to a future date.
     * </p>
     */
    @PastOrPresent(message = "Creation date cannot be in the future.")
    private LocalDateTime createdAt;

    /**
     * Date and time when the contract account was deleted or deactivated.
     * <p>
     * Optional — can be {@code null} for active accounts.
     * </p>
     */
    private LocalDateTime deletedAt;

    /**
     * Current status of the contract account.
     * <p>
     * Stored as a string value of {@link StatusType}.
     * </p>
     */
    @Enumerated(EnumType.STRING)
    private StatusType status;

    /**
     * Date and time when the current {@link #status} became effective.
     */
    private LocalDateTime statusStart;

    /**
     * Date and time when the current {@link #status} ended or will end.
     */
    private LocalDateTime statusEnd;

    // ---------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------

    /** @return the contract account ID */
    public Integer getId() {
        return id;
    }

    /** @param id sets the contract account ID */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the contract account number */
    public String getAccountNumber() {
        return accountNumber;
    }

    /** @param accountNumber sets the contract account number */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /** @return the associated client */
    public Client getClient() {
        return client;
    }

    /** @param client sets the associated client */
    public void setClient(Client client) {
        this.client = client;
    }

    /** @return the linked installation */
    public Installation getInstallation() {
        return installation;
    }

    /** @param installation sets the linked installation */
    public void setInstallation(Installation installation) {
        this.installation = installation;
    }

    /** @return the contract creation date */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt sets the contract creation date */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /** @return the contract deletion date (if applicable) */
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    /** @param deletedAt sets the contract deletion date */
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    /** @return the current status of the contract */
    public StatusType getStatus() {
        return status;
    }

    /** @param status sets the contract’s current status */
    public void setStatus(StatusType status) {
        this.status = status;
    }

    /** @return the start date of the current status */
    public LocalDateTime getStatusStart() {
        return statusStart;
    }

    /** @param statusStart sets the start date of the current status */
    public void setStatusStart(LocalDateTime statusStart) {
        this.statusStart = statusStart;
    }

    /** @return the end date of the current status */
    public LocalDateTime getStatusEnd() {
        return statusEnd;
    }

    /** @param statusEnd sets the end date of the current status */
    public void setStatusEnd(LocalDateTime statusEnd) {
        this.statusEnd = statusEnd;
    }

    @Override
    public String toString() {
        return "ContractAccount{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", client=" + (client != null ? client.getName() : "null") +
                ", installation=" + (installation != null ? installation.getId() : "null") +
                ", createdAt=" + createdAt +
                ", deletedAt=" + deletedAt +
                ", status=" + (status != null ? status.name() : "null") +
                ", statusStart=" + statusStart +
                ", statusEnd=" + statusEnd +
                '}';
    }
}
