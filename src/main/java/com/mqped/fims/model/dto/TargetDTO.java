package com.mqped.fims.model.dto;

import java.time.LocalDateTime;

import com.mqped.fims.model.entity.Target;
import com.mqped.fims.model.enums.TargetType;

public class TargetDTO {
    private Integer id;
    private Integer contractAccountId;
    private String contractAccountNumber;
    private TargetType type;
    private String signature;
    private Double score;
    private Double expectedCNR;
    private Double expectedTicket;
    private Double distanceFromBase;
    private LocalDateTime createdAt;
    private Boolean active;
    
    // Getters e setters
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContractAccountId() {
        return contractAccountId;
    }

    public void setContractAccountId(Integer contractAccountId) {
        this.contractAccountId = contractAccountId;
    }

    public String getContractAccountNumber() {
        return contractAccountNumber;
    }

    public void setContractAccountNumber(String contractAccountNumber) {
        this.contractAccountNumber = contractAccountNumber;
    }

    public TargetType getType() {
        return type;
    }

    public void setType(TargetType type) {
        this.type = type;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getExpectedCNR() {
        return expectedCNR;
    }

    public void setExpectedCNR(Double expectedCNR) {
        this.expectedCNR = expectedCNR;
    }

    public Double getExpectedTicket() {
        return expectedTicket;
    }

    public void setExpectedTicket(Double expectedTicket) {
        this.expectedTicket = expectedTicket;
    }

    public Double getDistanceFromBase() {
        return distanceFromBase;
    }

    public void setDistanceFromBase(Double distanceFromBase) {
        this.distanceFromBase = distanceFromBase;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public static TargetDTO fromEntity(Target target) {
        TargetDTO dto = new TargetDTO();
        dto.setId(target.getId());
        dto.setType(target.getType());
        dto.setSignature(target.getSignature());
        dto.setScore(target.getScore());
        dto.setExpectedCNR(target.getExpectedCNR());
        dto.setExpectedTicket(target.getExpectedTicket());
        dto.setDistanceFromBase(target.getDistanceFromBase());
        dto.setCreatedAt(target.getCreatedAt());
        dto.setActive(target.getActive());
        
        if (target.getContractAccount() != null) {
            dto.setContractAccountId(target.getContractAccount().getId());
            dto.setContractAccountNumber(target.getContractAccount().getAccountNumber());
        }
        
        return dto;
    }
}