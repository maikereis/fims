package com.mqped.fims.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "installations")
public class Installation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = { CascadeType.MERGE })
    @JoinColumn(name = "address_id")
    @JsonBackReference("address-installation")
    private Address address;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    // Backward reference to ContractAccounts
    @OneToMany(mappedBy = "installation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("installation-contractAccount")
    private List<ContractAccount> contractAccounts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<ContractAccount> getContractAccounts() {
        return contractAccounts;
    }

    public void setContractAccounts(List<ContractAccount> contractAccounts) {
        this.contractAccounts = contractAccounts;
    }

    @Override
    public String toString() {
        return "Installation{" +
                "id=" + id +
                ", address=" + (address != null ? address.toString() : "null") +
                ", createdAt=" + createdAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
