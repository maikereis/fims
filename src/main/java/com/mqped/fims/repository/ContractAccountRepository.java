package com.mqped.fims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mqped.fims.model.entity.ContractAccount;

/**
 * Repository interface for managing {@link ContractAccount} entities.
 * <p>
 * Extends {@link JpaRepository} to provide a complete set of standard
 * CRUD operations, along with pagination and sorting capabilities.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 * <li>Perform CRUD operations on {@link ContractAccount} entities</li>
 * <li>Serve as a data access layer for the contract-account relationship
 * domain</li>
 * </ul>
 *
 * <h3>Notes:</h3>
 * <ul>
 * <li>Custom query methods can be declared here if specific contract-account
 * lookups are needed</li>
 * <li>Typical relationships include links to {@code Client},
 * {@code Installation}, or {@code Target}</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * List<ContractAccount> accounts = contractAccountRepository.findAll();
 * ContractAccount account = contractAccountRepository.findById(1)
 *         .orElseThrow(() -> new ResourceNotFoundException("ContractAccount not found"));
 * }</pre>
 *
 * @see com.mqped.fims.model.entity.ContractAccount
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @since 1.0
 */
@Repository
public interface ContractAccountRepository extends JpaRepository<ContractAccount, Integer> {
    // JpaRepository already provides findAll, findById, save, deleteById,
    // existsById, etc.
}
