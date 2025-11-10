package com.mqped.fims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mqped.fims.model.entity.Installation;

/**
 * Repository interface for managing {@link Installation} entities.
 * <p>
 * Extends {@link JpaRepository} to provide a complete set of CRUD operations,
 * as well as pagination and sorting capabilities.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 * <li>Retrieve and manage installation records stored in the database</li>
 * <li>Provide efficient lookup methods for installations by related
 * {@code Address}</li>
 * <li>Support eager fetching of associated {@code ContractAccount} entities
 * when needed</li>
 * </ul>
 *
 * <h3>Custom Queries:</h3>
 * <ul>
 * <li>{@link #findByAddress_AddressId(String)} — retrieves installations by
 * their associated address ID</li>
 * <li>{@link #findAllByAddressIdWithContracts(String)} — retrieves
 * installations along with their related contract accounts</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * List<Installation> all = installationRepository.findAll();
 * List<Installation> byAddress = installationRepository.findByAddress_AddressId("ADDR123");
 * List<Installation> withContracts = installationRepository.findAllByAddressIdWithContracts("ADDR123");
 * }</pre>
 *
 * @see com.mqped.fims.model.entity.Installation
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @since 1.0
 */
@Repository
public interface InstallationRepository extends JpaRepository<Installation, Integer> {

    /**
     * Finds all installations associated with the given address identifier.
     *
     * @param addressId the unique address identifier
     * @return a list of installations associated with the address
     */
    List<Installation> findByAddress_AddressId(String addressId);

    /**
     * Finds all installations for the given address ID and eagerly fetches
     * their associated contract accounts to avoid N+1 query issues.
     *
     * @param addressId the unique address identifier
     * @return a list of installations with their related contract accounts loaded
     */
    @Query("""
            SELECT DISTINCT i
            FROM Installation i
            LEFT JOIN FETCH i.contractAccounts
            WHERE i.address.addressId = :addressId
            """)
    List<Installation> findAllByAddressIdWithContracts(@Param("addressId") String addressId);
}
