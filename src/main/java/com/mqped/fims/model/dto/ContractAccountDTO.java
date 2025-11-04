package com.mqped.fims.model.dto;

import com.mqped.fims.model.entity.ContractAccount;
import com.mqped.fims.model.enums.StatusType;
import java.time.LocalDateTime;

public class ContractAccountDTO {
    private Integer id;
    private String accountNumber;
    private Integer clientId;
    private String clientName;
    private Integer installationId;
    private InstallationDTO installation;
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

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getInstallationId() {
        return installationId;
    }

    public void setInstallationId(Integer installationId) {
        this.installationId = installationId;
    }

    public InstallationDTO getInstallation() {
        return installation;
    }

    public void setInstallation(InstallationDTO installation) {
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

    public static ContractAccountDTO fromEntity(ContractAccount contractAccount) {
        ContractAccountDTO dto = new ContractAccountDTO();
        dto.setId(contractAccount.getId());
        dto.setAccountNumber(contractAccount.getAccountNumber());
        dto.setCreatedAt(contractAccount.getCreatedAt());
        dto.setDeletedAt(contractAccount.getDeletedAt());
        dto.setStatus(contractAccount.getStatus());
        dto.setStatusStart(contractAccount.getStatusStart());
        dto.setStatusEnd(contractAccount.getStatusEnd());
        
        if (contractAccount.getClient() != null) {
            dto.setClientId(contractAccount.getClient().getId());
            dto.setClientName(contractAccount.getClient().getName());
        }
        
        if (contractAccount.getInstallation() != null) {
            dto.setInstallationId(contractAccount.getInstallation().getId());
            dto.setInstallation(InstallationDTO.fromEntity(contractAccount.getInstallation()));
        }
        
        return dto;
    }

    public static ContractAccountDTO fromEntityWithoutInstallation(ContractAccount contractAccount) {
        ContractAccountDTO dto = new ContractAccountDTO();
        dto.setId(contractAccount.getId());
        dto.setAccountNumber(contractAccount.getAccountNumber());
        dto.setCreatedAt(contractAccount.getCreatedAt());
        dto.setDeletedAt(contractAccount.getDeletedAt());
        dto.setStatus(contractAccount.getStatus());
        dto.setStatusStart(contractAccount.getStatusStart());
        dto.setStatusEnd(contractAccount.getStatusEnd());
        
        if (contractAccount.getClient() != null) {
            dto.setClientId(contractAccount.getClient().getId());
            dto.setClientName(contractAccount.getClient().getName());
        }
        
        if (contractAccount.getInstallation() != null) {
            dto.setInstallationId(contractAccount.getInstallation().getId());
        }
        
        return dto;
    }
}