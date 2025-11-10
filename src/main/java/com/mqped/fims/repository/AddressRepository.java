package com.mqped.fims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mqped.fims.model.entity.Address;

import java.util.Optional;

/**
 * Repository interface for managing {@link Address} entities.
 * <p>
 * Provides standard CRUD operations through {@link JpaRepository}, as well as
 * an additional query method to locate addresses by their business identifier.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 * <li>Provide data persistence and retrieval for {@link Address} entities</li>
 * <li>Allow lookups by both primary key and business-specific address ID</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * Optional<Address> address = addressRepository.findByAddressId("123-456-XYZ");
 * address.ifPresent(a -> System.out.println(a.getStreet()));
 * }</pre>
 *
 * @see com.mqped.fims.model.entity.Address
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @since 1.0
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    /**
     * Retrieves an {@link Address} by its unique business identifier.
     *
     * @param addressId the unique address identifier (not the primary key)
     * @return an {@link Optional} containing the matching {@link Address}, or empty
     *         if none found
     */
    Optional<Address> findByAddressId(String addressId);
}
