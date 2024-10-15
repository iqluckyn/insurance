package com.iql.policyadmin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.iql.policyadmin.domain.QuotationStatus} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private String statusName;

    @NotNull
    private String statusCode;

    private String description;

    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuotationStatusDTO)) {
            return false;
        }

        QuotationStatusDTO quotationStatusDTO = (QuotationStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quotationStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotationStatusDTO{" +
            "id=" + getId() +
            ", statusName='" + getStatusName() + "'" +
            ", statusCode='" + getStatusCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
