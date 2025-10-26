package com.mqped.fims.service;

import com.mqped.fims.model.Installation;
import com.mqped.fims.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class InstallationService implements CrudRepository<Installation, Integer> {
    private final Map<Integer, Installation> hashMap = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public Installation add(Installation installation) {
        installation.setId(nextId.getAndIncrement());
        hashMap.put(installation.getId(), installation);
        return installation;
    }

    @Override
    public List<Installation> findAll() {
        return new ArrayList<>(hashMap.values());
    }

    @Override
    public Optional<Installation> findById(Integer id) {
        return Optional.ofNullable(hashMap.get(id));
    }

    @Override
    public Optional<Installation> update(Integer id, Installation installation) {
        if (!hashMap.containsKey(id)) {
            return Optional.empty();
        }
        installation.setId(id); // ensure ID consistency
        hashMap.put(id, installation);
        return Optional.of(installation);
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
