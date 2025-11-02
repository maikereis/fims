package com.mqped.fims.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.mqped.fims.model.ContractAccount;
import com.mqped.fims.repository.CrudRepository;

@Service
public class ContractAccountService implements CrudRepository<ContractAccount, Integer> {
    private final Map<Integer, ContractAccount> hashMap = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public ContractAccount add(ContractAccount contractAccount) {
        contractAccount.setId(nextId.getAndIncrement());
        hashMap.put(contractAccount.getId(), contractAccount);
        return contractAccount;
    }

    @Override
    public List<ContractAccount> findAll() {
        return new ArrayList<>(hashMap.values());
    }

    @Override
    public Optional<ContractAccount> findById(Integer id) {
        return Optional.ofNullable(hashMap.get(id));
    }

    @Override
    public Optional<ContractAccount> update(Integer id, ContractAccount contractAccount) {
        if (!hashMap.containsKey(id)) {
            return Optional.empty();
        }
        contractAccount.setId(id);
        hashMap.put(id, contractAccount);
        return Optional.of(contractAccount);
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

    public Map<Integer, ContractAccount> getHashMap() {
        return hashMap;
    }

    public AtomicInteger getNextId() {
        return nextId;
    }

}
