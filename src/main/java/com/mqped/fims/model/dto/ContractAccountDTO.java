package com.mqped.fims.model.dto;

import com.mqped.fims.model.entity.ContractAccount;
import com.mqped.fims.model.enums.StatusType;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a {@link ContractAccount} entity.
 * <p>
 * The {@code ContractAccountDTO} class is used to expose contract account data
 * in a simplified form between application layers, avoiding direct exposure of
 * the persistence entity. It includes details such as account number, client
 * information, installation details, lifecycle timestamps, and account status.
 * </p>
 *
 * <h2>Usage Example</h2>
 * 
 * <pre>{@code
 * ContractAccount entity = contractAccountRepository.findById(1).orElseThrow();
 * ContractAccountDTO dto = ContractAccountDTO.fromEntity(entity);
 * System.out.println("Account number: " + dto.getAccountNumber());
 * }</pre>
 *
 * @see com.mqped.fims.model.entity.ContractAccount
 * @see com.mqped.fims.model.dto.InstallationDTO
 * @see com.mqped.fims.model.enums.StatusType
 */
public class ContractAccountDTO {

    /** Unique identifier of the contract account. */
    private Integer id;

    /** Account number associated with the contract. */
    private String accountNumber;

    /** Unique identifier of the associated client. */
    private Integer clientId;

    /** Name of the associated client. */
    private String clientName;

    /** Unique identifier of the associated installation. */
    private Integer installationId;

    /** Installation details associated with the contract account. */
    private InstallationDTO installation;

    /** Date and time when the contract account was created. */
    private LocalDateTime createdAt;

    /** Date and time when the contract account was deleted (if applicable). */
    private LocalDateTime deletedAt;

    /** Current status of the contract account (e.g., ACTIVE, INACTIVE). */
    private StatusType status;

    /** Date and time when the current status became effective. */
    private LocalDateTime statusStart;

    /** Date and time when the current status ended (if applicable). */
    private LocalDateTime statusEnd;

    // --- Getters and Setters ---

    /** @return the unique identifier of the contract account. */
    public Integer getId() {
        return id;
    }

    /** @param id sets the unique identifier of the contract account. */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the account number associated with this contract. */
    public String getAccountNumber() {
        return accountNumber;
    }

    /** @param accountNumber sets the account number for this contract. */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /** @return the unique identifier of the client. */
    public Integer getClientId() {
        return clientId;
    }

    /** @param clientId sets the unique identifier of the client. */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /** @return the full name of the client. */
    public String getClientName() {
        return clientName;
    }

    /** @param clientName sets the full name of the client. */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /** @return the unique identifier of the associated installation. */
    public Integer getInstallationId() {
        return installationId;
    }

    /** @param installationId sets the unique identifier of the installation. */
    public void setInstallationId(Integer installationId) {
        this.installationId = installationId;
    }

    /** @return the installation details associated with this account. */
    public InstallationDTO getInstallation() {
        return installation;
    }

    /** @param installation sets the installation details. */
    public void setInstallation(InstallationDTO installation) {
        this.installation = installation;
    }

    /** @return the timestamp when the account was created. */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt sets the timestamp when the account was created. */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /** @return the timestamp when the account was deleted (if applicable). */
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    /** @param deletedAt sets the timestamp when the account was deleted. */
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    /** @return the current status of the contract account. */
    public StatusType getStatus() {
        return status;
    }

    /** @param status sets the current status of the contract account. */
    public void setStatus(StatusType status) {
        this.status = status;
    }

    /** @return the date and time when the current status began. */
    public LocalDateTime getStatusStart() {
        return statusStart;
    }

    /** @param statusStart sets the date and time when the current status began. */
    public void setStatusStart(LocalDateTime statusStart) {
        this.statusStart = statusStart;
    }

    /** @return the date and time when the current status ended (if applicable). */
    public LocalDateTime getStatusEnd() {
        return statusEnd;
    }

    /** @param statusEnd sets the date and time when the current status ended. */
    public void setStatusEnd(LocalDateTime statusEnd) {
        this.statusEnd = statusEnd;
    }

    // --- Factory Methods ---

    /**
     * Converts a {@link ContractAccount} entity into a {@link ContractAccountDTO},
     * including installation details.
     *
     * @param contractAccount the {@link ContractAccount} entity to convert
     * @return a {@link ContractAccountDTO} populated with data from the entity
     */
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

    /**
     * Converts a {@link ContractAccount} entity into a {@link ContractAccountDTO}
     * without including installation details.
     * <p>
     * This method is useful for lightweight data transfers where installation
     * details are not needed.
     * </p>
     *
     * @param contractAccount the {@link ContractAccount} entity to convert
     * @return a {@link ContractAccountDTO} without installation data
     */
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
