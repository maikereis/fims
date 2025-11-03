package com.mqped.fims.service;

import com.mqped.fims.exceptions.InvalidDataException;
import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.Target;
import com.mqped.fims.model.TargetType;
import com.mqped.fims.repository.ContractAccountRepository;
import com.mqped.fims.repository.TargetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TargetService implements CrudService<Target, Integer> {

    private final TargetRepository repository;
    private final ContractAccountRepository contractAccountRepository;

    public TargetService(TargetRepository repository, ContractAccountRepository contractAccountRepository) {
        this.repository = repository;
        this.contractAccountRepository = contractAccountRepository;
    }

    @Override
    public Target add(Target target) {
        validate(target);

        Integer contractId = target.getContractAccount().getId();
        if (contractId == null || !contractAccountRepository.existsById(contractId)) {
            throw new InvalidDataException("ContractAccount with id " + contractId + " does not exist");
        }

        return repository.save(target);
    }

    public List<Target> findAll() {
        return repository.findAll();
    }

    public Optional<Target> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Target> update(Integer id, Target target) {
        validate(target);

        return repository.findById(id).map(existing -> {
            existing.setContractAccount(target.getContractAccount());
            existing.setType(target.getType());
            existing.setExpectedCNR(target.getExpectedCNR());
            existing.setExpectedTicket(target.getExpectedTicket());
            existing.setSignature(target.getSignature());
            existing.setScore(target.getScore());
            existing.setActive(target.getActive());
            return repository.save(existing);
        });
    }

    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Target with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    public long count() {
        return repository.count();
    }

    public List<Target> findByContractAccountId(Integer contractAccountId) {
        return repository.findByContractAccountId(contractAccountId);
    }

    public List<Target> findByClientId(Integer clientId) {
        return repository.findByContractAccountClientId(clientId);
    }

    public List<Target> findByType(TargetType type) {
        return repository.findByType(type);
    }

    public List<Target> findBySignature(String signature) {
        return repository.findBySignature(signature);
    }

    public List<Target> findBySignatureContaining(String partialSignature) {
        return repository.findBySignatureContaining(partialSignature);
    }

    public List<Target> findByScoreGreater(Double score) {
        return repository.findByScoreGreaterThan(score);
    }

    public List<Target> findByScoreLess(Double score) {
        return repository.findByScoreLessThan(score);
    }

    public List<Target> findByScoreBetween(Double min, Double max) {
        return repository.findByScoreBetween(min, max);
    }

    public List<Target> findByDistanceLess(Double maxDistance) {
        return repository.findByDistanceFromBaseLessThan(maxDistance);
    }

    public List<Target> findByDistanceGreater(Double minDistance) {
        return repository.findByDistanceFromBaseGreaterThan(minDistance);
    }

    public List<Target> findByDistanceBetween(Double minDistance, Double maxDistance) {
        return repository.findByDistanceFromBaseBetween(minDistance, maxDistance);
    }

    public void validate(Target target) {
        if (target == null)
            throw new InvalidDataException("Target cannot be null");
        if (target.getContractAccount() == null)
            throw new InvalidDataException("ContractAccount is required");
        if (target.getType() == null)
            throw new InvalidDataException("Target type is required");
        if (target.getExpectedCNR() != null && target.getExpectedCNR() < 0)
            throw new InvalidDataException("Expected CNR cannot be negative");
        if (target.getExpectedTicket() != null && target.getExpectedTicket() < 0)
            throw new InvalidDataException("Expected ticket cannot be negative");
        if (target.getScore() != null && target.getScore() < 0)
            throw new InvalidDataException("Score cannot be negative");
    }
}
