package com.mqped.fims.repository;

import com.mqped.fims.model.entity.ServiceOrder;
import com.mqped.fims.model.enums.ServiceOrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Integer> {

    List<ServiceOrder> findByStatus(ServiceOrderStatus status);

    List<ServiceOrder> findByTargetId(Integer targetId);

    List<ServiceOrder> findByTargetDistanceFromBaseLessThan(Double maxDistance);

    List<ServiceOrder> findByTargetDistanceFromBaseGreaterThan(Double minDistance);

    List<ServiceOrder> findByTargetDistanceFromBaseBetween(Double minDistance, Double maxDistance);

    List<ServiceOrder> findByTargetSignature(String signature);

    List<ServiceOrder> findByTargetSignatureContaining(String partial);

    @Query("SELECT so FROM ServiceOrder so WHERE so.createdAt <= :cutoff")
    List<ServiceOrder> findOlderThan(LocalDateTime cutoff);

    @Query("SELECT so FROM ServiceOrder so WHERE so.createdAt BETWEEN :start AND :end")
    List<ServiceOrder> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
