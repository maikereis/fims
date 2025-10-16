package com.mqped.fims.service;

import com.mqped.fims.model.Address;
import com.mqped.fims.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AddressService implements CrudRepository<Address, Integer> {
    private final Map<Integer, Address> hashMap = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public Address add(Address address) {
        address.setId(nextId.getAndIncrement());
        hashMap.put(address.getId(), address);
        return address;
    }

    @Override
    public List<Address> findAll() {
        return new ArrayList<>(hashMap.values());
    }

    @Override
    public Optional<Address> findById(Integer id) {
        return Optional.ofNullable(hashMap.get(id));
    }

    @Override
    public Optional<Address> update(Integer id, Address address) {
        if (!hashMap.containsKey(id)) {
            return Optional.empty();
        }
        address.setId(id); // ensure ID consistency
        hashMap.put(id, address);
        return Optional.of(address);
    }

    @Override
    public boolean deleteById(Integer id) {
        return hashMap.remove(id) != null;
    }

    @Override
    public boolean existsById(Integer id) {
        return hashMap.containsKey(id);
    }

    @Override
    public long count() {
        return hashMap.size();
    }
}
