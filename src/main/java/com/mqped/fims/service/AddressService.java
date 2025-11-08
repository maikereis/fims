package com.mqped.fims.service;

import com.mqped.fims.exceptions.InvalidDataException;
import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.Address;
import com.mqped.fims.model.entity.Installation;
import com.mqped.fims.repository.AddressRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService implements CrudService<Address, Integer> {

    private final AddressRepository repository;

    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public Address add(Address address) {
        validate(address);

        if (address.getInstallations() != null) {
            for (Installation inst : address.getInstallations()) {
                inst.setAddress(address);
                inst.setId(null);
            }
        }

        return repository.save(address);
    }

    @Override
    public List<Address> findAll() {
        return repository.findAll();
    }

    @Override
    public Address findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address with id " + id + " not found"));
    }

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

    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Address with id " + id + " not found");
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

    public Address findByAddressId(String addressId) {
        return repository.findByAddressId(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address with addressId " + addressId + " not found"));
    }

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