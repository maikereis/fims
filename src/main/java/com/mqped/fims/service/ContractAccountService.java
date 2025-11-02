package com.mqped.fims.service;

import com.mqped.fims.model.ContractAccount;
import com.mqped.fims.repository.ContractAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContractAccountService implements CrudService<ContractAccount, Integer> {

    private final ContractAccountRepository repository;

    public ContractAccountService(ContractAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public ContractAccount add(ContractAccount contractAccount) {
        validate(contractAccount);
        return repository.save(contractAccount);
    }

    @Override
    public List<ContractAccount> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ContractAccount> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Optional<ContractAccount> update(Integer id, ContractAccount contractAccount) {
        validate(contractAccount);

        return repository.findById(id).map(existing -> {
            existing.setAccountNumber(contractAccount.getAccountNumber());
            existing.setClient(contractAccount.getClient());
            existing.setInstallation(contractAccount.getInstallation());
            existing.setCreatedAt(contractAccount.getCreatedAt());
            existing.setDeletedAt(contractAccount.getDeletedAt());
            existing.setStatus(contractAccount.getStatus());
            existing.setStatusStart(contractAccount.getStatusStart());
            existing.setStatusEnd(contractAccount.getStatusEnd());

            return repository.save(existing);
        });
    }

    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("ContractAccount with id " + id + " not found");
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
            throw new IllegalArgumentException("ContractAccount cannot be null");
        }
        if (contractAccount.getAccountNumber() == null || contractAccount.getAccountNumber().isBlank()) {
            throw new IllegalArgumentException("Account number is required");
        }
        if (contractAccount.getClient() == null) {
            throw new IllegalArgumentException("Client is required");
        }
        if (contractAccount.getInstallation() == null) {
            throw new IllegalArgumentException("Installation is required");
        }
        if (contractAccount.getCreatedAt() == null) {
            throw new IllegalArgumentException("Creation date is required");
        }
        // Optional: add more rules for status, statusStart, statusEnd, etc.
    }
}
