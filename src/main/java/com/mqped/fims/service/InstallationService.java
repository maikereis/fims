package com.mqped.fims.service;

import com.mqped.fims.exceptions.InvalidDataException;
import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.Address;
import com.mqped.fims.model.entity.Installation;
import com.mqped.fims.repository.AddressRepository;
import com.mqped.fims.repository.InstallationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing {@link Installation} entities.
 * <p>
 * This class provides high-level business logic for CRUD operations involving
 * {@link Installation} entities, ensuring data integrity, validation, and
 * relationship management with {@link Address} entities.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 * <li>CRUD operations for installations</li>
 * <li>Validation of installation data before persistence</li>
 * <li>Association management between {@link Installation} and
 * {@link Address}</li>
 * </ul>
 *
 * <h3>Exception Handling:</h3>
 * <ul>
 * <li>{@link InvalidDataException} – Thrown for invalid or incomplete data</li>
 * <li>{@link ResourceNotFoundException} – Thrown when the referenced entity is
 * not found</li>
 * </ul>
 *
 * @see com.mqped.fims.model.entity.Installation
 * @see com.mqped.fims.model.entity.Address
 * @see com.mqped.fims.repository.InstallationRepository
 * @see com.mqped.fims.repository.AddressRepository
 * @see CrudService
 *
 * @since 1.0
 */
@Service
public class InstallationService implements CrudService<Installation, Integer> {

    private final InstallationRepository repository;
    private final AddressRepository addressRepository;

    /**
     * Constructs a new {@code InstallationService} with the provided repositories.
     *
     * @param repository        the repository for managing {@link Installation}
     *                          entities
     * @param addressRepository the repository for managing related {@link Address}
     *                          entities
     */
    public InstallationService(InstallationRepository repository, AddressRepository addressRepository) {
        this.repository = repository;
        this.addressRepository = addressRepository;
    }

    /**
     * Adds a new {@link Installation} to the system after validation.
     *
     * @param installation the installation to add
     * @return the persisted installation
     * @throws InvalidDataException if required data (e.g., address or creation
     *                              date) is missing or invalid
     */
    @Override
    public Installation add(Installation installation) {
        validate(installation);

        if (installation.getCreatedAt() == null) {
            throw new InvalidDataException("Installation creation date is required");
        }

        return repository.save(installation);
    }

    /**
     * Retrieves all installations.
     *
     * @return a list of all installations
     */
    @Override
    public List<Installation> findAll() {
        return repository.findAll();
    }

    /**
     * Finds an installation by its unique identifier.
     *
     * @param id the ID of the installation to find
     * @return the found installation
     * @throws ResourceNotFoundException if no installation exists with the given ID
     */
    @Override
    public Installation findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Installation with id " + id + " not found"));
    }

    /**
     * Updates an existing {@link Installation} with new data.
     * <p>
     * Validates the provided installation and ensures that its associated
     * {@link Address}
     * exists in the database before saving the update.
     * </p>
     *
     * @param id           the ID of the installation to update
     * @param installation the updated installation data
     * @return the updated installation
     * @throws InvalidDataException      if validation fails or the address does not
     *                                   exist
     * @throws ResourceNotFoundException if the installation to update does not
     *                                   exist
     */
    @Override
    public Installation update(Integer id, Installation installation) {
        validate(installation);

        Installation existing = findById(id); // throws if not found

        Address managedAddress = addressRepository.findById(installation.getAddress().getId())
                .orElseThrow(() -> new InvalidDataException("Address not found"));

        existing.setAddress(managedAddress);
        // existing.setCreatedAt(installation.getCreatedAt());
        existing.setDeletedAt(installation.getDeletedAt());

        return repository.save(existing);
    }

    /**
     * Deletes an installation by its ID.
     *
     * @param id the ID of the installation to delete
     * @throws ResourceNotFoundException if the installation does not exist
     */
    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Installation with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    /**
     * Checks whether an installation with the given ID exists.
     *
     * @param id the ID to check
     * @return {@code true} if the installation exists, {@code false} otherwise
     */
    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    /**
     * Counts the total number of installations in the system.
     *
     * @return the total count of installations
     */
    @Override
    public long count() {
        return repository.count();
    }

    /**
     * Retrieves all installations associated with a given address ID.
     *
     * @param addressId the external address ID to search for
     * @return a list of installations associated with the specified address
     */
    public List<Installation> findByAddressId(String addressId) {
        return repository.findByAddress_AddressId(addressId);
    }

    /**
     * Retrieves all installations for a given address ID,
     * including their associated contract accounts.
     *
     * @param addressId the external address ID
     * @return a list of installations with their contracts eagerly loaded
     */
    public List<Installation> findByAddressIdWithContracts(String addressId) {
        return repository.findAllByAddressIdWithContracts(addressId);
    }

    /**
     * Validates the integrity and completeness of an {@link Installation} entity.
     *
     * @param installation the installation to validate
     * @throws InvalidDataException if the installation or its required fields are
     *                              null
     */
    private void validate(Installation installation) {
        if (installation == null) {
            throw new InvalidDataException("Installation cannot be null");
        }
        if (installation.getAddress() == null) {
            throw new InvalidDataException("Installation must have an address");
        }
    }
}
