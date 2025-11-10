package com.mqped.fims.service;

import com.mqped.fims.exceptions.InvalidDataException;
import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.Target;
import com.mqped.fims.model.enums.TargetType;
import com.mqped.fims.repository.ContractAccountRepository;
import com.mqped.fims.repository.TargetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing {@link Target} entities.
 * <p>
 * Provides CRUD operations and domain-specific query methods for targets,
 * including
 * validation of contract associations, target metrics, and integrity
 * constraints.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 * <li>Create, update, delete, and query {@link Target} records</li>
 * <li>Validate required fields and numerical constraints</li>
 * <li>Filter targets by client, contract, score, distance, and type</li>
 * </ul>
 *
 * <h3>Exception Handling:</h3>
 * <ul>
 * <li>{@link InvalidDataException} – Thrown when provided target data is
 * incomplete or inconsistent</li>
 * <li>{@link ResourceNotFoundException} – Thrown when the associated contract
 * or target does not exist</li>
 * </ul>
 *
 * @see com.mqped.fims.model.entity.Target
 * @see com.mqped.fims.model.enums.TargetType
 * @see com.mqped.fims.repository.TargetRepository
 * @see CrudService
 *
 * @since 1.0
 */
@Service
public class TargetService implements CrudService<Target, Integer> {

    private final TargetRepository repository;
    private final ContractAccountRepository contractAccountRepository;

    /**
     * Constructs a new {@code TargetService} instance.
     *
     * @param repository                the repository managing {@link Target}
     *                                  entities
     * @param contractAccountRepository the repository managing associated contract
     *                                  accounts
     */
    public TargetService(TargetRepository repository, ContractAccountRepository contractAccountRepository) {
        this.repository = repository;
        this.contractAccountRepository = contractAccountRepository;
    }

    /**
     * Creates a new {@link Target} after validating its data and contract
     * association.
     *
     * @param target the target to persist
     * @return the persisted {@link Target}
     * @throws InvalidDataException      if the target is invalid
     * @throws ResourceNotFoundException if the associated contract does not exist
     */
    @Override
    public Target add(Target target) {
        validate(target);

        Integer contractId = target.getContractAccount().getId();
        if (contractId == null || !contractAccountRepository.existsById(contractId)) {
            throw new ResourceNotFoundException("ContractAccount with id " + contractId + " not found");
        }

        return repository.save(target);
    }

    /**
     * Retrieves all targets.
     *
     * @return a list of all {@link Target} records
     */
    @Override
    public List<Target> findAll() {
        return repository.findAll();
    }

    /**
     * Retrieves a {@link Target} by its unique identifier.
     *
     * @param id the target ID
     * @return the matching {@link Target}
     * @throws ResourceNotFoundException if no target exists with the given ID
     */
    @Override
    public Target findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Target with id " + id + " not found"));
    }

    /**
     * Updates an existing {@link Target}.
     *
     * @param id     the ID of the target to update
     * @param target the new target data
     * @return the updated {@link Target}
     * @throws InvalidDataException      if the new data is invalid
     * @throws ResourceNotFoundException if the target or its associated contract
     *                                   does not exist
     */
    @Override
    public Target update(Integer id, Target target) {
        validate(target);

        Target existing = findById(id); // throws if not found

        Integer contractId = target.getContractAccount().getId();
        if (contractId == null || !contractAccountRepository.existsById(contractId)) {
            throw new ResourceNotFoundException("ContractAccount with id " + contractId + " not found");
        }

        existing.setContractAccount(target.getContractAccount());
        existing.setType(target.getType());
        existing.setExpectedCNR(target.getExpectedCNR());
        existing.setExpectedTicket(target.getExpectedTicket());
        existing.setDistanceFromBase(target.getDistanceFromBase());
        existing.setSignature(target.getSignature());
        existing.setScore(target.getScore());
        existing.setActive(target.getActive());

        return repository.save(existing);
    }

    /**
     * Deletes a {@link Target} by ID.
     *
     * @param id the target ID
     * @throws ResourceNotFoundException if the target does not exist
     */
    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Target with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    /**
     * Checks if a {@link Target} with the given ID exists.
     *
     * @param id the ID to check
     * @return {@code true} if the target exists, otherwise {@code false}
     */
    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    /**
     * Counts all {@link Target} records.
     *
     * @return the total number of targets
     */
    @Override
    public long count() {
        return repository.count();
    }

    // -------------------------------------------------------------
    // Query Methods
    // -------------------------------------------------------------

    /**
     * Retrieves all targets associated with a specific contract account.
     *
     * @param contractAccountId the contract account ID
     * @return a list of matching {@link Target} entities
     */
    public List<Target> findByContractAccountId(Integer contractAccountId) {
        return repository.findByContractAccountId(contractAccountId);
    }

    /**
     * Retrieves all targets associated with a specific client.
     *
     * @param clientId the client ID
     * @return a list of matching {@link Target} entities
     */
    public List<Target> findByClientId(Integer clientId) {
        return repository.findByContractAccountClientId(clientId);
    }

    /**
     * Finds targets by their {@link TargetType}.
     *
     * @param type the target type
     * @return a list of targets with the given type
     */
    public List<Target> findByType(TargetType type) {
        return repository.findByType(type);
    }

    /**
     * Finds targets matching a specific signature.
     *
     * @param signature the signature to search for
     * @return a list of matching {@link Target} entities
     */
    public List<Target> findBySignature(String signature) {
        return repository.findBySignature(signature);
    }

    /**
     * Finds targets whose signature contains a specific substring.
     *
     * @param partialSignature a partial signature string
     * @return a list of matching {@link Target} entities
     */
    public List<Target> findBySignatureContaining(String partialSignature) {
        return repository.findBySignatureContaining(partialSignature);
    }

    /**
     * Finds targets with a score greater than the specified value.
     *
     * @param score the minimum score
     * @return a list of matching {@link Target} entities
     */
    public List<Target> findByScoreGreater(Double score) {
        return repository.findByScoreGreaterThan(score);
    }

    /**
     * Finds targets with a score less than the specified value.
     *
     * @param score the maximum score
     * @return a list of matching {@link Target} entities
     */
    public List<Target> findByScoreLess(Double score) {
        return repository.findByScoreLessThan(score);
    }

    /**
     * Finds targets whose score is between two values.
     *
     * @param min the minimum score
     * @param max the maximum score
     * @return a list of targets whose score is between {@code min} and {@code max}
     */
    public List<Target> findByScoreBetween(Double min, Double max) {
        return repository.findByScoreBetween(min, max);
    }

    /**
     * Finds targets whose distance from base is less than the specified maximum.
     *
     * @param maxDistance the maximum distance
     * @return a list of matching {@link Target} entities
     */
    public List<Target> findByDistanceLess(Double maxDistance) {
        return repository.findByDistanceFromBaseLessThan(maxDistance);
    }

    /**
     * Finds targets whose distance from base is greater than the specified minimum.
     *
     * @param minDistance the minimum distance
     * @return a list of matching {@link Target} entities
     */
    public List<Target> findByDistanceGreater(Double minDistance) {
        return repository.findByDistanceFromBaseGreaterThan(minDistance);
    }

    /**
     * Finds targets whose distance from base is between two values.
     *
     * @param minDistance the minimum distance
     * @param maxDistance the maximum distance
     * @return a list of matching {@link Target} entities
     */
    public List<Target> findByDistanceBetween(Double minDistance, Double maxDistance) {
        return repository.findByDistanceFromBaseBetween(minDistance, maxDistance);
    }

    // -------------------------------------------------------------
    // Validation
    // -------------------------------------------------------------

    /**
     * Validates a {@link Target} entity for required fields and logical
     * consistency.
     *
     * @param target the target to validate
     * @throws InvalidDataException if any validation rule is violated
     */
    private void validate(Target target) {
        if (target == null) {
            throw new InvalidDataException("Target cannot be null");
        }
        if (target.getContractAccount() == null) {
            throw new InvalidDataException("ContractAccount is required");
        }
        if (target.getType() == null) {
            throw new InvalidDataException("Target type is required");
        }
        if (target.getExpectedCNR() != null && target.getExpectedCNR() < 0) {
            throw new InvalidDataException("Expected CNR cannot be negative");
        }
        if (target.getExpectedTicket() != null && target.getExpectedTicket() < 0) {
            throw new InvalidDataException("Expected ticket cannot be negative");
        }
        if (target.getScore() != null && target.getScore() < 0) {
            throw new InvalidDataException("Score cannot be negative");
        }
        if (target.getDistanceFromBase() != null && target.getDistanceFromBase() < 0) {
            throw new InvalidDataException("Distance from base cannot be negative");
        }
    }
}
