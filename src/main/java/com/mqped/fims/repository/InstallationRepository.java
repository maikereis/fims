package com.mqped.fims.repository;

import com.mqped.fims.model.Installation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallationRepository extends JpaRepository<Installation, Integer> {
    // JpaRepository already provides findAll, findById, save, deleteById, etc.
    List<Installation> findByAddress_AddressId(String addressId);

    @Query("""
                SELECT DISTINCT i
                FROM Installation i
                LEFT JOIN FETCH i.contractAccounts
                WHERE i.address.addressId = :addressId
            """)
    List<Installation> findAllByAddressIdWithContracts(@Param("addressId") String addressId);
}
