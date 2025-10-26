package com.mqped.fims.service;

import com.mqped.fims.model.Client;
import com.mqped.fims.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ClientService implements CrudRepository<Client, Integer> {

    private final Map<Integer, Client> hashMap = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public Client add(Client client) {
        client.setId(nextId.getAndIncrement());
        hashMap.put(client.getId(), client);
        return client;
    }

    @Override
    public List<Client> findAll() {
        return new ArrayList<>(hashMap.values());
    }

    @Override
    public Optional<Client> findById(Integer id) {
        return Optional.ofNullable(hashMap.get(id));
    }

    @Override
    public Optional<Client> update(Integer id, Client client) {
        if (!hashMap.containsKey(id)) {
            return Optional.empty();
        }
        client.setId(id); // ensure ID consistency
        hashMap.put(id, client);
        return Optional.of(client);
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
