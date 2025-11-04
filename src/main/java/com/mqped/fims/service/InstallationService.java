package com.mqped.fims.service;

import com.mqped.fims.exceptions.InvalidDataException;
import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.Installation;
import com.mqped.fims.repository.InstallationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstallationService implements CrudService<Installation, Integer> {

    private final InstallationRepository repository;

    public InstallationService(InstallationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Installation add(Installation installation) {
        validate(installation);
        return repository.save(installation);
    }

    @Override
    public List<Installation> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Installation> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Installation> update(Integer id, Installation installation) {
        validate(installation);

        return repository.findById(id).map(existing -> {
            existing.setAddress(installation.getAddress());
            existing.setCreatedAt(installation.getCreatedAt());
            existing.setDeletedAt(installation.getDeletedAt());
            // Add more fields if your Installation model expands

            return repository.save(existing);
        });
    }

    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Installation with id " + id + " not found");
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

    public List<Installation> findByAddressId(String addressId) {
        return repository.findByAddress_AddressId(addressId);
    }

    public List<Installation> findByAddressIdWithContracts(String addressId) {
        return repository.findAllByAddressIdWithContracts(addressId);
    }

    private void validate(Installation installation) {
        if (installation == null) {
            throw new InvalidDataException("Installation cannot be null");
        }
        if (installation.getAddress() == null) {
            throw new InvalidDataException("Installation must have an address");
        }
        if (installation.getCreatedAt() == null) {
            throw new InvalidDataException("Installation creation date is required");
        }
    }
}
