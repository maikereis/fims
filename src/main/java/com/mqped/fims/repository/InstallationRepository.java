package com.mqped.fims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mqped.fims.model.entity.Installation;

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
