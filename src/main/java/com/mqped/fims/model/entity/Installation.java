package com.mqped.fims.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.mqped.fims.validation.annotation.ChronologicalDates;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

/**
 * Represents an installation entity in the system.
 * <p>
 * Each {@code Installation} is linked to a specific {@link Address} and can be
 * associated with multiple {@link ContractAccount} records.
 * </p>
 *
 * <p>
 * The entity also includes creation and deletion timestamps, which are
 * validated
 * using the {@link ChronologicalDates} annotation to ensure the deletion date
 * is
 * chronologically after the creation date.
 * </p>
 *
 * <h2>Validation Rules</h2>
 * <ul>
 * <li>{@code createdAt} cannot be in the future.</li>
 * <li>{@code deletedAt} (if set) must be equal to or after
 * {@code createdAt}.</li>
 * <li>{@code address} is mandatory.</li>
 * </ul>
 *
 * <h2>Relationships</h2>
 * <ul>
 * <li>{@link Address} – Many installations belong to one address.</li>
 * <li>{@link ContractAccount} – One installation may have many contract
 * accounts.</li>
 * </ul>
 */
@Entity
@Table(name = "installations")
@ChronologicalDates(start = "createdAt", end = "deletedAt", allowEqual = true, message = "Deleted date must be after creation date.")
public class Installation {

    /** Primary key identifier for the installation. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The physical address associated with this installation.
     * <p>
     * Cannot be null.
     * </p>
     */
    @ManyToOne(cascade = { CascadeType.MERGE })
    @JoinColumn(name = "address_id", nullable = false)
    @NotNull(message = "Address is required.")
    private Address address;

    /**
     * Date and time when this installation was created. Must not be in the future.
     */
    @PastOrPresent(message = "Creation date cannot be in the future.")
    private LocalDateTime createdAt;

    /** Date and time when this installation was deleted. Optional. */
    private LocalDateTime deletedAt;

    /**
     * List of contract accounts associated with this installation.
     * <p>
     * Cascade and orphan removal are enabled to ensure data consistency.
     * </p>
     */
    @OneToMany(mappedBy = "installation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractAccount> contractAccounts;

    /** @return the unique identifier of the installation. */
    public Integer getId() {
        return id;
    }

    /** @param id sets the unique identifier of the installation. */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the {@link Address} linked to this installation. */
    public Address getAddress() {
        return address;
    }

    /** @param address sets the {@link Address} linked to this installation. */
    public void setAddress(Address address) {
        this.address = address;
    }

    /** @return the creation date and time of this installation. */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt sets the creation date and time of this installation. */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /** @return the deletion date and time of this installation, if applicable. */
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    /** @param deletedAt sets the deletion date and time of this installation. */
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * @return list of {@link ContractAccount} entities linked to this installation.
     */
    public List<ContractAccount> getContractAccounts() {
        return contractAccounts;
    }

    /**
     * @param contractAccounts sets the list of {@link ContractAccount} linked to
     *                         this installation.
     */
    public void setContractAccounts(List<ContractAccount> contractAccounts) {
        this.contractAccounts = contractAccounts;
    }

    /**
     * Returns a string representation of the installation for logging or debugging.
     * 
     * @return formatted string with installation details
     */
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
