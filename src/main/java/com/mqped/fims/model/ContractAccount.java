package com.mqped.fims.model;

import java.time.LocalDateTime;

public class ContractAccount {

    private Integer id;
    private String accountNumber;
    private Client client;
    private Installation installation;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
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
