package com.iql.policyadmin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.iql.policyadmin.domain.Business} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusinessDTO implements Serializable {

    private Long id;

    @NotNull
    private String registeredName;

    private String organisationName;

    private String vatNumber;

    private Instant createdAt;

    private Instant updatedAt;

    private BusinessTypeDTO businessType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegisteredName() {
        return registeredName;
    }

    public void setRegisteredName(String registeredName) {
        this.registeredName = registeredName;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BusinessTypeDTO getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessTypeDTO businessType) {
        this.businessType = businessType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessDTO)) {
            return false;
        }

        BusinessDTO businessDTO = (BusinessDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, businessDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessDTO{" +
            "id=" + getId() +
            ", registeredName='" + getRegisteredName() + "'" +
            ", organisationName='" + getOrganisationName() + "'" +
            ", vatNumber='" + getVatNumber() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", businessType=" + getBusinessType() +
            "}";
    }
}
