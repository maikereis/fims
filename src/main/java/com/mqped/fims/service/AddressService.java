package com.mqped.fims.service;

import com.mqped.fims.model.Address;
import com.mqped.fims.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    private final AddressRepository repository;

    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    public Address add(Address address) {
        return repository.save(address);
    }

    public List<Address> findAll() {
        return repository.findAll();
    }

    public Optional<Address> findById(String id) {
        return repository.findById(id);
    }

    public Address save(Address address) {
        return repository.save(address);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

}
