package com.mqped.fims.repository;

import com.mqped.fims.model.Target;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mqped.fims.model.TargetType;

@Repository
public interface TargetRepository extends JpaRepository<Target, Integer> {
    // JpaRepository already provides findAll, findById, save, deleteById, etc.
    List<Target> findByContractAccountId(Integer contractAccountId);

    List<Target> findByContractAccountClientId(Integer clientId);

    List<Target> findByType(TargetType type);

    List<Target> findBySignature(String signature);

    List<Target> findByScoreGreaterThan(Double score);

    List<Target> findByScoreLessThan(Double score);

    List<Target> findByScoreBetween(Double min, Double max);

    List<Target> findBySignatureContaining(String partialSignature);

    List<Target> findByDistanceFromBaseLessThan(Double maxDistance);

    List<Target> findByDistanceFromBaseGreaterThan(Double minDistance);

    List<Target> findByDistanceFromBaseBetween(Double minDistance, Double maxDistance);
}
