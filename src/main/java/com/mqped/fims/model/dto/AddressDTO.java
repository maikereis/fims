package com.mqped.fims.model.dto;

import com.mqped.fims.model.entity.Address;

/**
 * Data Transfer Object (DTO) representing an {@link Address} entity.
 * <p>
 * The {@code AddressDTO} class is used to transfer address data between
 * application layers (e.g., controllers, services, and clients) without
 * exposing
 * the JPA entity directly. It contains basic information about an address,
 * including location details, identifiers, and optional geolocation
 * coordinates.
 * </p>
 *
 * <h2>Usage Example</h2>
 * 
 * <pre>{@code
 * Address address = addressRepository.findById(1).orElseThrow();
 * AddressDTO dto = AddressDTO.fromEntity(address);
 * System.out.println(dto.getStreet() + ", " + dto.getNumber());
 * }</pre>
 *
 * @see com.mqped.fims.model.entity.Address
 */
public class AddressDTO {

    /** Unique identifier of the address. */
    private Integer id;

    /** Custom address identifier (for external systems or tracking). */
    private String addressId;

    /** State or province where the address is located. */
    private String state;

    /** Municipality or city of the address. */
    private String municipality;

    /** District or administrative subdivision (optional). */
    private String district;

    /** Subdistrict or smaller administrative division (optional). */
    private String subdistrict;

    /** Neighborhood name of the address. */
    private String neighborhood;

    /** Street name of the address. */
    private String street;

    /** Street type (e.g., Avenue, Street, Road). */
    private String streetType;

    /** Street number of the address. */
    private String number;

    /**
     * Additional address details such as apartment or building number (optional).
     */
    private String complement;

    /** ZIP or postal code of the address. */
    private String zipCode;

    /** Geographic latitude coordinate (optional). */
    private Double latitude;

    /** Geographic longitude coordinate (optional). */
    private Double longitude;

    // --- Getters and Setters ---

    /** @return the unique identifier of the address. */
    public Integer getId() {
        return id;
    }

    /** @param id sets the unique identifier of the address. */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the custom address identifier. */
    public String getAddressId() {
        return addressId;
    }

    /** @param addressId sets the custom address identifier. */
    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    /** @return the state or province name. */
    public String getState() {
        return state;
    }

    /** @param state sets the state or province name. */
    public void setState(String state) {
        this.state = state;
    }

    /** @return the municipality or city. */
    public String getMunicipality() {
        return municipality;
    }

    /** @param municipality sets the municipality or city. */
    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    /** @return the district name, if applicable. */
    public String getDistrict() {
        return district;
    }

    /** @param district sets the district name. */
    public void setDistrict(String district) {
        this.district = district;
    }

    /** @return the subdistrict name, if applicable. */
    public String getSubdistrict() {
        return subdistrict;
    }

    /** @param subdistrict sets the subdistrict name. */
    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    /** @return the neighborhood name. */
    public String getNeighborhood() {
        return neighborhood;
    }

    /** @param neighborhood sets the neighborhood name. */
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    /** @return the street name. */
    public String getStreet() {
        return street;
    }

    /** @param street sets the street name. */
    public void setStreet(String street) {
        this.street = street;
    }

    /** @return the street type (e.g., Avenue, Street, Road). */
    public String getStreetType() {
        return streetType;
    }

    /** @param streetType sets the street type. */
    public void setStreetType(String streetType) {
        this.streetType = streetType;
    }

    /** @return the street number. */
    public String getNumber() {
        return number;
    }

    /** @param number sets the street number. */
    public void setNumber(String number) {
        this.number = number;
    }

    /** @return any additional address complement information. */
    public String getComplement() {
        return complement;
    }

    /** @param complement sets additional address complement information. */
    public void setComplement(String complement) {
        this.complement = complement;
    }

    /** @return the ZIP or postal code. */
    public String getZipCode() {
        return zipCode;
    }

    /** @param zipCode sets the ZIP or postal code. */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /** @return the geographic latitude coordinate, if available. */
    public Double getLatitude() {
        return latitude;
    }

    /** @param latitude sets the geographic latitude coordinate. */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /** @return the geographic longitude coordinate, if available. */
    public Double getLongitude() {
        return longitude;
    }

    /** @param longitude sets the geographic longitude coordinate. */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Converts an {@link Address} entity to an {@link AddressDTO}.
     *
     * @param address the {@link Address} entity to convert
     * @return an {@link AddressDTO} populated with data from the entity
     */
    public static AddressDTO fromEntity(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setAddressId(address.getAddressId());
        dto.setState(address.getState());
        dto.setMunicipality(address.getMunicipality());
        dto.setDistrict(address.getDistrict());
        dto.setSubdistrict(address.getSubdistrict());
        dto.setNeighborhood(address.getNeighborhood());
        dto.setStreet(address.getStreet());
        dto.setStreetType(address.getStreetType());
        dto.setNumber(address.getNumber());
        dto.setComplement(address.getComplement());
        dto.setZipCode(address.getZipCode());
        dto.setLatitude(address.getLatitude());
        dto.setLongitude(address.getLongitude());
        return dto;
    }
}
