package com.mqped.fims.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Represents a physical or geographical address within the system.
 * <p>
 * The {@code Address} entity stores full address details such as state,
 * municipality,
 * neighborhood, street, number, and optional geographic coordinates.
 * </p>
 *
 * <p>
 * <strong>Database table:</strong> {@code addresses}
 * </p>
 *
 * <p>
 * Each {@code Address} may be associated with multiple {@link Installation}
 * entities.
 * </p>
 *
 * @author MQPED
 * @see Installation
 */
@Entity
@Table(name = "addresses")
public class Address {

    /**
     * Unique identifier for the address record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Optional custom address identifier used for integration or external
     * references.
     */
    private String addressId;

    /**
     * The state or province where the address is located.
     * <p>
     * Cannot be blank.
     * </p>
     */
    @NotBlank(message = "State is required.")
    private String state;

    /**
     * The municipality or city of the address.
     * <p>
     * Cannot be blank.
     * </p>
     */
    @NotBlank(message = "Municipality is required.")
    private String municipality;

    /**
     * The district, if applicable, within the municipality.
     * <p>
     * Optional field.
     * </p>
     */
    private String district;

    /**
     * The subdistrict or local administrative division.
     * <p>
     * Optional field.
     * </p>
     */
    private String subdistrict;

    /**
     * The neighborhood or community where the address is located.
     * <p>
     * Cannot be blank.
     * </p>
     */
    @NotBlank(message = "Neighborhood is required.")
    private String neighborhood;

    /**
     * The street name of the address.
     * <p>
     * Cannot be blank.
     * </p>
     */
    @NotBlank(message = "Street is required.")
    private String street;

    /**
     * The type of street (e.g., Avenue, Road, Street, Boulevard).
     * <p>
     * Optional field.
     * </p>
     */
    private String streetType;

    /**
     * The building or house number.
     * <p>
     * Cannot be blank.
     * </p>
     */
    @NotBlank(message = "Number is required.")
    private String number;

    /**
     * Additional information or details about the address, such as apartment or
     * unit number.
     */
    private String complement;

    /**
     * Postal code (ZIP code) of the address.
     * <p>
     * Cannot be blank.
     * </p>
     */
    @NotBlank(message = "ZIP code is required.")
    private String zipCode;

    /**
     * Latitude coordinate of the address location.
     * <p>
     * Optional — used for geolocation or mapping.
     * </p>
     */
    private Double latitude;

    /**
     * Longitude coordinate of the address location.
     * <p>
     * Optional — used for geolocation or mapping.
     * </p>
     */
    private Double longitude;

    /**
     * List of installations located at this address.
     * <p>
     * Cascade and orphan removal are enabled — deleting an address will also remove
     * its associated installations.
     * </p>
     */
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Installation> installations = new ArrayList<>();

    // ---------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------

    /** @return the address database ID */
    public Integer getId() {
        return id;
    }

    /** @param id sets the address database ID */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the external or custom address ID */
    public String getAddressId() {
        return addressId;
    }

    /** @param addressId sets the external or custom address ID */
    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    /** @return the state name */
    public String getState() {
        return state;
    }

    /** @param state sets the state name */
    public void setState(String state) {
        this.state = state;
    }

    /** @return the municipality or city */
    public String getMunicipality() {
        return municipality;
    }

    /** @param municipality sets the municipality or city */
    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    /** @return the district name, if applicable */
    public String getDistrict() {
        return district;
    }

    /** @param district sets the district name */
    public void setDistrict(String district) {
        this.district = district;
    }

    /** @return the subdistrict name, if applicable */
    public String getSubdistrict() {
        return subdistrict;
    }

    /** @param subdistrict sets the subdistrict name */
    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    /** @return the neighborhood name */
    public String getNeighborhood() {
        return neighborhood;
    }

    /** @param neighborhood sets the neighborhood name */
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    /** @return the street name */
    public String getStreet() {
        return street;
    }

    /** @param street sets the street name */
    public void setStreet(String street) {
        this.street = street;
    }

    /** @return the street type (e.g., Avenue, Street) */
    public String getStreetType() {
        return streetType;
    }

    /** @param streetType sets the street type */
    public void setStreetType(String streetType) {
        this.streetType = streetType;
    }

    /** @return the building or house number */
    public String getNumber() {
        return number;
    }

    /** @param number sets the building or house number */
    public void setNumber(String number) {
        this.number = number;
    }

    /** @return the complement or additional address details */
    public String getComplement() {
        return complement;
    }

    /** @param complement sets the complement or additional address details */
    public void setComplement(String complement) {
        this.complement = complement;
    }

    /** @return the ZIP or postal code */
    public String getZipCode() {
        return zipCode;
    }

    /** @param zipCode sets the ZIP or postal code */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /** @return the latitude coordinate */
    public Double getLatitude() {
        return latitude;
    }

    /** @param latitude sets the latitude coordinate */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /** @return the longitude coordinate */
    public Double getLongitude() {
        return longitude;
    }

    /** @param longitude sets the longitude coordinate */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /** @return the list of installations at this address */
    public List<Installation> getInstallations() {
        return installations;
    }

    /** @param installations sets the list of installations at this address */
    public void setInstallations(List<Installation> installations) {
        this.installations = installations;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", addressId='" + addressId + '\'' +
                ", state='" + state + '\'' +
                ", municipality='" + municipality + '\'' +
                ", district='" + district + '\'' +
                ", subdistrict='" + subdistrict + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                ", street='" + street + '\'' +
                ", streetType='" + streetType + '\'' +
                ", number='" + number + '\'' +
                ", complement='" + complement + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
