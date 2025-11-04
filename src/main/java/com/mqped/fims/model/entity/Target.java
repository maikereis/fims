package com.mqped.fims.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.mqped.fims.model.enums.TargetType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import jakarta.persistence.*;

@Entity
@Table(name = "targets")
public class Target {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_account_id", nullable = false)
    private ContractAccount contractAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType type;

    private String signature;

    @NotNull
    @Min(value = 0)
    private Double score;

    @NotNull
    @Min(value = 0)
    private Double expectedCNR;

    @NotNull
    @Min(value = 0)
    private Double expectedTicket;

    @NotNull
    @Min(value = 0)
    private Double distanceFromBase;

    private LocalDateTime createdAt;
    private Boolean active = true;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceOrder> serviceOrders;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ContractAccount getContractAccount() {
        return contractAccount;
    }

    public void setContractAccount(ContractAccount contractAccount) {
        this.contractAccount = contractAccount;
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

    public List<ServiceOrder> getServiceOrders() {
        return serviceOrders;
    }

    public void setServiceOrders(List<ServiceOrder> serviceOrders) {
        this.serviceOrders = serviceOrders;
    }

    @Override
    public String toString() {
        return "Target{" +
                "id=" + id +
                ", contractAccount=" + (contractAccount != null ? contractAccount.getId() : "null") +
                ", type=" + type +
                ", expectedCNR=" + expectedCNR +
                ", expectedTicket=" + expectedTicket +
                ", distanceFromBase=" + distanceFromBase +
                ", active=" + active +
                '}';
    }
}
