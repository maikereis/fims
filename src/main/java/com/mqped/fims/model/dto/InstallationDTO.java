package com.mqped.fims.model.dto;

import com.mqped.fims.model.entity.Installation;
import java.time.LocalDateTime;

public class InstallationDTO {
    private Integer id;
    private Integer addressId;
    private AddressDTO address;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
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