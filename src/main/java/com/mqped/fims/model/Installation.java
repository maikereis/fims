package com.mqped.fims.model;

import java.time.LocalDateTime;

public class Installation {

    private Integer id;
    private Address address;
    private LocalDateTime createAt;
    private LocalDateTime deletedAt;

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

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return "Installation{" +
                "id=" + id +
                ", address=" + (address != null ? address.toString() : "null") +
                ", createAt=" + createAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
