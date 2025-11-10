package com.mqped.fims.model.dto;

import com.mqped.fims.model.entity.Installation;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a {@link Installation} entity.
 * <p>
 * The {@code InstallationDTO} class is used to transfer installation data
 * between
 * layers of the application without exposing the underlying entity. It
 * encapsulates
 * key installation attributes such as associated address information, creation,
 * and
 * deletion timestamps.
 * </p>
 *
 * <h2>Usage Example</h2>
 * 
 * <pre>{@code
 * Installation entity = installationRepository.findById(1).orElseThrow();
 * InstallationDTO dto = InstallationDTO.fromEntity(entity);
 * System.out.println("Installation ID: " + dto.getId());
 * }</pre>
 *
 * @see com.mqped.fims.model.entity.Installation
 * @see com.mqped.fims.model.dto.AddressDTO
 */
public class InstallationDTO {

    /** Unique identifier of the installation. */
    private Integer id;

    /** Unique identifier of the associated address. */
    private Integer addressId;

    /** Address details associated with the installation. */
    private AddressDTO address;

    /** Timestamp indicating when the installation record was created. */
    private LocalDateTime createdAt;

    /** Timestamp indicating when the installation was deleted (if applicable). */
    private LocalDateTime deletedAt;

    // --- Getters and Setters ---

    /** @return the unique identifier of the installation. */
    public Integer getId() {
        return id;
    }

    /** @param id sets the unique identifier of the installation. */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the unique identifier of the associated address. */
    public Integer getAddressId() {
        return addressId;
    }

    /** @param addressId sets the unique identifier of the associated address. */
    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    /** @return the address details linked to this installation. */
    public AddressDTO getAddress() {
        return address;
    }

    /** @param address sets the address details for this installation. */
    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    /** @return the timestamp when the installation was created. */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt sets the timestamp when the installation was created. */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /** @return the timestamp when the installation was deleted (if applicable). */
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    /** @param deletedAt sets the timestamp when the installation was deleted. */
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    // --- Factory Methods ---

    /**
     * Converts a {@link Installation} entity into an {@link InstallationDTO},
     * including address details.
     *
     * @param installation the {@link Installation} entity to convert
     * @return an {@link InstallationDTO} containing the mapped data
     */
    public static InstallationDTO fromEntity(Installation installation) {
        InstallationDTO dto = new InstallationDTO();
        dto.setId(installation.getId());
        dto.setCreatedAt(installation.getCreatedAt());
        dto.setDeletedAt(installation.getDeletedAt());

        if (installation.getAddress() != null) {
            dto.setAddressId(installation.getAddress().getId());
            dto.setAddress(AddressDTO.fromEntity(installation.getAddress()));
        }

        return dto;
    }

    /**
     * Converts a {@link Installation} entity into an {@link InstallationDTO}
     * without including address details.
     * <p>
     * This method is useful for lightweight responses where full address data
     * is not required.
     * </p>
     *
     * @param installation the {@link Installation} entity to convert
     * @return an {@link InstallationDTO} without address data
     */
    public static InstallationDTO fromEntityWithoutAddress(Installation installation) {
        InstallationDTO dto = new InstallationDTO();
        dto.setId(installation.getId());
        dto.setCreatedAt(installation.getCreatedAt());
        dto.setDeletedAt(installation.getDeletedAt());

        if (installation.getAddress() != null) {
            dto.setAddressId(installation.getAddress().getId());
        }

        return dto;
    }
}
