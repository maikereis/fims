package com.mqped.fims.repository;

import com.mqped.fims.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    // JpaRepository already provides findAll, findById, save, deleteById, etc.
}
