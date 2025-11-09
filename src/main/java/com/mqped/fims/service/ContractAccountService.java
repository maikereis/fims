package com.mqped.fims.service;

import com.mqped.fims.exceptions.InvalidDataException;
import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.ContractAccount;
import com.mqped.fims.repository.ClientRepository;
import com.mqped.fims.repository.ContractAccountRepository;
import com.mqped.fims.repository.InstallationRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractAccountService implements CrudService<ContractAccount, Integer> {

    private final ContractAccountRepository repository;
    private final ClientRepository clientRepository;
    private final InstallationRepository installationRepository;

    public ContractAccountService(
            ContractAccountRepository repository,
            ClientRepository clientRepository,
            InstallationRepository installationRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
        this.installationRepository = installationRepository;
    }

    @Override
    public ContractAccount add(ContractAccount contractAccount) {
        validate(contractAccount);

        // Aditional validation needed only when addind new contracts
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

    @Override
    public List<ContractAccount> findAll() {
        return repository.findAll();
    }

    @Override
    public ContractAccount findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContractAccount with id " + id + " not found"));
    }

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

    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ContractAccount with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

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