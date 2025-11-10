package com.mqped.fims.repository;

import com.mqped.fims.model.entity.Target;
import com.mqped.fims.model.enums.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link Target} entities.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations and
 * several domain-specific query methods for analytics, filtering,
 * and operational monitoring.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 * <li>Query targets by related entities (ContractAccount, Client)</li>
 * <li>Filter by numeric and categorical attributes (score, distance, type)</li>
 * <li>Support lookup by signature or pattern matching</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * List<Target> highScore = targetRepository.findByScoreGreaterThan(80.0);
 * List<Target> lowDistance = targetRepository.findByDistanceFromBaseLessThan(5.0);
 * List<Target> billingTargets = targetRepository.findByType(TargetType.BILLING);
 * }</pre>
 *
 * @see com.mqped.fims.model.entity.Target
 * @see com.mqped.fims.model.enums.TargetType
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @since 1.0
 */
@Repository
public interface TargetRepository extends JpaRepository<Target, Integer> {

    /**
     * Retrieves all targets linked to a given contract account.
     *
     * @param contractAccountId the ID of the contract account
     * @return list of targets under the given contract
     */
    List<Target> findByContractAccountId(Integer contractAccountId);

    /**
     * Retrieves all targets associated with a given client.
     *
     * @param clientId the client's ID
     * @return list of targets belonging to the client's accounts
     */
    List<Target> findByContractAccountClientId(Integer clientId);

    /**
     * Retrieves targets of the specified {@link TargetType}.
     *
     * @param type the type of target (e.g. INSTALLATION, INSPECTION)
     * @return list of targets matching the type
     */
    List<Target> findByType(TargetType type);

    /**
     * Finds a target by its unique signature (if applicable).
     *
     * @param signature the exact signature value
     * @return list of matching targets (typically size 1)
     */
    List<Target> findBySignature(String signature);

    /**
     * Retrieves all targets where the signature partially matches a given
     * substring.
     */
    List<Target> findBySignatureContaining(String partialSignature);

    /**
     * Retrieves targets whose score is greater than the given value.
     */
    List<Target> findByScoreGreaterThan(Double score);

    /**
     * Retrieves targets whose score is less than the given value.
     */
    List<Target> findByScoreLessThan(Double score);

    /**
     * Retrieves targets with scores between the given range (inclusive).
     */
    List<Target> findByScoreBetween(Double min, Double max);

    /**
     * Retrieves targets within a specified distance from base (less than
     * maxDistance).
     */
    List<Target> findByDistanceFromBaseLessThan(Double maxDistance);

    /**
     * Retrieves targets beyond a specified minimum distance from base.
     */
    List<Target> findByDistanceFromBaseGreaterThan(Double minDistance);

    /**
     * Retrieves targets within a range of distances from base (inclusive).
     */
    List<Target> findByDistanceFromBaseBetween(Double minDistance, Double maxDistance);
}
