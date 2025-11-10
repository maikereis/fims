package com.mqped.fims.service;

import com.mqped.fims.exceptions.InvalidDataException;
import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.ContractAccount;
import com.mqped.fims.repository.ClientRepository;
import com.mqped.fims.repository.ContractAccountRepository;
import com.mqped.fims.repository.InstallationRepository;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing {@link ContractAccount} entities.
 * <p>
 * Provides CRUD operations as defined in {@link CrudService} and enforces
 * domain-specific validation rules, including:
 * <ul>
 * <li>Verification of required fields (account number, client,
 * installation)</li>
 * <li>Validation of associated {@link com.mqped.fims.model.entity.Client} and
 * {@link com.mqped.fims.model.entity.Installation} existence</li>
 * <li>Ensuring that new contract accounts include a creation date</li>
 * </ul>
 * </p>
 *
 * <p>
 * Exceptions thrown:
 * <ul>
 * <li>{@link InvalidDataException} — when provided data is null or fails
 * validation</li>
 * <li>{@link ResourceNotFoundException} — when referenced entities (client,
 * installation, or contract account) cannot be found</li>
 * </ul>
 * </p>
 *
 * <p>
 * This service acts as a domain layer between controllers and persistence,
 * coordinating repository operations across multiple related entities.
 * </p>
 *
 * @author
 * @since 1.0
 */
@Service
public class ContractAccountService implements CrudService<ContractAccount, Integer> {

    private final ContractAccountRepository repository;
    private final ClientRepository clientRepository;
    private final InstallationRepository installationRepository;

    /**
     * Constructs a new {@code ContractAccountService} with the provided
     * repositories.
     *
     * @param repository             the {@link ContractAccountRepository} used for
     *                               data persistence
     * @param clientRepository       the {@link ClientRepository} used to verify
     *                               client existence
     * @param installationRepository the {@link InstallationRepository} used to
     *                               verify installation existence
     */
    public ContractAccountService(
            ContractAccountRepository repository,
            ClientRepository clientRepository,
            InstallationRepository installationRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
        this.installationRepository = installationRepository;
    }

    /**
     * Persists a new {@link ContractAccount} after validating required fields
     * and ensuring that related entities exist.
     * <p>
     * Additional validation ensures that the contract's creation date is provided.
     * </p>
     *
     * @param contractAccount the contract account entity to persist
     * @return the saved {@link ContractAccount}
     * @throws InvalidDataException      if required data is missing or invalid
     * @throws ResourceNotFoundException if related {@code Client} or
     *                                   {@code Installation} does not exist
     */
    @Override
    public ContractAccount add(ContractAccount contractAccount) {
        validate(contractAccount);

        // Additional validation required for new contract creation
        if (contractAccount.getCreatedAt() == null) {
            throw new InvalidDataException("Creation date is required");
        }

        if (!clientRepository.existsById(contractAccount.getClient().getId())) {
            throw new ResourceNotFoundException(
                    "Client with id " + contractAccount.getClient().getId() + " not found");
        }

        if (!installationRepository.existsById(contractAccount.getInstallation().getId())) {
            throw new ResourceNotFoundException(
                    "Installation with id " + contractAccount.getInstallation().getId() + " not found");
        }

        return repository.save(contractAccount);
    }

    /**
     * Retrieves all contract accounts from the repository.
     *
     * @return list of all {@link ContractAccount} entities
     */
    @Override
    public List<ContractAccount> findAll() {
        return repository.findAll();
    }

    /**
     * Finds a {@link ContractAccount} by its unique integer ID.
     *
     * @param id the contract account ID
     * @return the found {@link ContractAccount}
     * @throws ResourceNotFoundException if no contract account with the given ID
     *                                   exists
     */
    @Override
    public ContractAccount findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContractAccount with id " + id + " not found"));
    }

    /**
     * Updates an existing {@link ContractAccount} with new field values.
     * <p>
     * This method validates the provided data, verifies that the referenced
     * client and installation exist, and then applies updates to status-related
     * fields.
     * Immutable fields such as account number and creation date are intentionally
     * not modified.
     * </p>
     *
     * @param id              the ID of the contract account to update
     * @param contractAccount the updated contract account data
     * @return the updated {@link ContractAccount}
     * @throws InvalidDataException      if validation fails
     * @throws ResourceNotFoundException if the contract account, client, or
     *                                   installation does not exist
     */
    @Override
    public ContractAccount update(Integer id, ContractAccount contractAccount) {
        validate(contractAccount);

        ContractAccount existing = findById(id); // throws if not found

        if (!clientRepository.existsById(contractAccount.getClient().getId())) {
            throw new ResourceNotFoundException(
                    "Client with id " + contractAccount.getClient().getId() + " not found");
        }

        if (!installationRepository.existsById(contractAccount.getInstallation().getId())) {
            throw new ResourceNotFoundException(
                    "Installation with id " + contractAccount.getInstallation().getId() + " not found");
        }

        // Immutable fields are intentionally not updated
        // existing.setAccountNumber(contractAccount.getAccountNumber());
        // existing.setClient(contractAccount.getClient());
        // existing.setInstallation(contractAccount.getInstallation());
        // existing.setCreatedAt(contractAccount.getCreatedAt());

        existing.setDeletedAt(contractAccount.getDeletedAt());
        existing.setStatus(contractAccount.getStatus());
        existing.setStatusStart(contractAccount.getStatusStart());
        existing.setStatusEnd(contractAccount.getStatusEnd());

        return repository.save(existing);
    }

    /**
     * Deletes a contract account by its ID.
     *
     * @param id the ID of the contract account to delete
     * @throws ResourceNotFoundException if no contract account with the given ID
     *                                   exists
     */
    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ContractAccount with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    /**
     * Checks whether a {@link ContractAccount} exists by its ID.
     *
     * @param id the contract account ID
     * @return {@code true} if exists, {@code false} otherwise
     */
    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    /**
     * Counts all {@link ContractAccount} records in the repository.
     *
     * @return total number of contract accounts
     */
    @Override
    public long count() {
        return repository.count();
    }

    /**
     * Validates required fields for a {@link ContractAccount}.
     * <ul>
     * <li>Ensures the contract account object is not null</li>
     * <li>Ensures the account number is present</li>
     * <li>Ensures both client and installation references are provided</li>
     * </ul>
     *
     * @param contractAccount the contract account to validate
     * @throws InvalidDataException if validation fails
     */
    private void validate(ContractAccount contractAccount) {
        if (contractAccount == null) {
            throw new InvalidDataException("ContractAccount cannot be null");
        }
        if (contractAccount.getAccountNumber() == null || contractAccount.getAccountNumber().isBlank()) {
            throw new InvalidDataException("Account number is required");
        }
        if (contractAccount.getClient() == null) {
            throw new InvalidDataException("Client is required");
        }
        if (contractAccount.getInstallation() == null) {
            throw new InvalidDataException("Installation is required");
        }
    }
}
