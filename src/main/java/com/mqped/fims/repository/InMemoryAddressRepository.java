package com.mqped.fims.repository;

import com.mqped.fims.model.Address;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAddressRepository implements CrudRepository<Address, Integer> {

    private static final String ILLEGAL_ARGUMENT_MESSAGE = "Address ID cannot be null";

    private final Map<Integer, Address> storage = new ConcurrentHashMap<>();

    @Override
    public Address add(Address entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE);
        }
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public List<Address> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Address> findById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Address> update(Integer id, Address entity) {
        if (!storage.containsKey(id)) {
            return Optional.empty();
        }
        entity.setId(id);
        storage.put(id, entity);
        return Optional.of(entity);
    }

    @Override
    public boolean deleteById(Integer id) {
        return storage.remove(id) != null;
    }

    @Override
    public boolean existsById(Integer id) {
        return storage.containsKey(id);
    }

    @Override
    public long count() {
        return storage.size();
    }
}