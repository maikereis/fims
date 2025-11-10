package com.mqped.fims.service;

import com.mqped.fims.exceptions.InvalidDataException;
import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.Address;
import com.mqped.fims.model.entity.Installation;
import com.mqped.fims.repository.AddressRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class responsible for managing {@link Address} entities.
 * <p>
 * This class implements generic CRUD operations defined by {@link CrudService}
 * and provides additional domain-specific logic such as:
 * <ul>
 * <li>Validation of required address fields before persistence</li>
 * <li>Linking {@link Installation} entities to their parent
 * {@link Address}</li>
 * <li>Lookup by both numeric ID and external address identifier</li>
 * </ul>
 * </p>
 *
 * <p>
 * Exceptions thrown:
 * <ul>
 * <li>{@link InvalidDataException} — if provided data is null or invalid</li>
 * <li>{@link ResourceNotFoundException} — if a requested resource does not
 * exist</li>
 * </ul>
 * </p>
 *
 * <p>
 * This service acts as a domain layer between controllers and the
 * {@link AddressRepository}.
 * </p>
 *
 * @author
 * @since 1.0
 */
@Service
public class AddressService implements CrudService<Address, Integer> {

    private final AddressRepository repository;

    /**
     * Constructs a new {@code AddressService} with the provided repository.
     *
     * @param repository the {@link AddressRepository} used for data persistence
     */
    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    /**
     * Persists a new {@link Address} after validating its fields and
     * linking any contained {@link Installation} entities.
     *
     * @param address the address entity to persist
     * @return the saved {@link Address} entity
     * @throws InvalidDataException if validation fails or address is null
     */
    @Override
    public Address add(Address address) {
        validate(address);

        if (address.getInstallations() != null) {
            for (Installation inst : address.getInstallations()) {
                inst.setAddress(address);
                inst.setId(null); // Ensures new Installation entities are created
            }
        }

        return repository.save(address);
    }

    /**
     * Retrieves all addresses from the database.
     *
     * @return list of all {@link Address} entities
     */
    @Override
    public List<Address> findAll() {
        return repository.findAll();
    }

    /**
     * Finds an address by its unique integer ID.
     *
     * @param id the address ID
     * @return the found {@link Address}
     * @throws ResourceNotFoundException if no address with the given ID exists
     */
    @Override
    public Address findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address with id " + id + " not found"));
    }

    /**
     * Updates an existing {@link Address} with new field values.
     * <p>
     * This method retrieves the existing entity, applies field updates, and saves
     * it back.
     * The provided {@code address} object is not directly persisted.
     * </p>
     *
     * @param id      ID of the address to update
     * @param address the new address data
     * @return the updated {@link Address} entity
     * @throws InvalidDataException      if validation fails
     * @throws ResourceNotFoundException if the address does not exist
     */
    @Override
    public Address update(Integer id, Address address) {
        validate(address);

        Address existing = findById(id); // throws if not found

        existing.setAddressId(address.getAddressId());
        existing.setState(address.getState());
        existing.setMunicipality(address.getMunicipality());
        existing.setDistrict(address.getDistrict());
        existing.setSubdistrict(address.getSubdistrict());
        existing.setNeighborhood(address.getNeighborhood());
        existing.setStreet(address.getStreet());
        existing.setStreetType(address.getStreetType());
        existing.setNumber(address.getNumber());
        existing.setComplement(address.getComplement());
        existing.setZipCode(address.getZipCode());
        existing.setLatitude(address.getLatitude());
        existing.setLongitude(address.getLongitude());

        return repository.save(existing);
    }

    /**
     * Deletes an address by its ID.
     *
     * @param id the ID of the address to delete
     * @throws ResourceNotFoundException if the address does not exist
     */
    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Address with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    /**
     * Checks if an address exists by its ID.
     *
     * @param id the ID to check
     * @return {@code true} if exists, {@code false} otherwise
     */
    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    /**
     * Counts all {@link Address} records in the repository.
     *
     * @return total number of addresses
     */
    @Override
    public long count() {
        return repository.count();
    }

    /**
     * Finds an {@link Address} by its external string identifier (addressId).
     *
     * @param addressId the external address identifier
     * @return the found {@link Address}
     * @throws ResourceNotFoundException if not found
     */
    public Address findByAddressId(String addressId) {
        return repository.findByAddressId(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address with addressId " + addressId + " not found"));
    }

    /**
     * Validates that required address fields are present.
     *
     * @param address the address to validate
     * @throws InvalidDataException if required fields are missing or blank
     */
    private void validate(Address address) {
        if (address == null) {
            throw new InvalidDataException("Address cannot be null");
        }
        if (address.getState() == null || address.getState().isBlank()) {
            throw new InvalidDataException("State is required");
        }
        if (address.getMunicipality() == null || address.getMunicipality().isBlank()) {
            throw new InvalidDataException("Municipality is required");
        }
        if (address.getStreet() == null || address.getStreet().isBlank()) {
            throw new InvalidDataException("Street is required");
        }
    }
}
