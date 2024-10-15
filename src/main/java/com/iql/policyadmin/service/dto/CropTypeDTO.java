package com.iql.policyadmin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.iql.policyadmin.domain.CropType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CropTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String cropName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CropTypeDTO)) {
            return false;
        }

        CropTypeDTO cropTypeDTO = (CropTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cropTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CropTypeDTO{" +
            "id=" + getId() +
            ", cropName='" + getCropName() + "'" +
            "}";
    }
}
