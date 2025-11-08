package com.mqped.fims.model.entity;

import java.time.LocalDateTime;

import com.mqped.fims.model.enums.StatusType;
import com.mqped.fims.validation.annotation.ChronologicalDates;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "contract_accounts")
@ChronologicalDates(start = "createdAt", end = "deletedAt", allowEqual = true, message = "Deleted date must be after creation date.")
@ChronologicalDates(start = "statusStart", end = "statusEnd", allowEqual = true, message = "Status end date must be after status start date.")
@ChronologicalDates(start = "createdAt", end = "statusStart", allowEqual = true, message = "Status start date must be after creation date.")
public class ContractAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String accountNumber;

    @ManyToOne(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = "Client is required.")
    private Client client;

    @ManyToOne(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinColumn(name = "installation_id", nullable = false)
    @NotNull(message = "Installation is required.")
    private Installation installation;

    @PastOrPresent(message = "Creation date cannot be in the future.")
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    private StatusType status;
    private LocalDateTime statusStart;
    private LocalDateTime statusEnd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Installation getInstallation() {
        return installation;
    }

    public void setInstallation(Installation installation) {
        this.installation = installation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public LocalDateTime getStatusStart() {
        return statusStart;
    }

    public void setStatusStart(LocalDateTime statusStart) {
        this.statusStart = statusStart;
    }

    public LocalDateTime getStatusEnd() {
        return statusEnd;
    }

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
