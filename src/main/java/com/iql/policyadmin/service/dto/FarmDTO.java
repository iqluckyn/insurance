package com.iql.policyadmin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.iql.policyadmin.domain.Farm} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FarmDTO implements Serializable {

    private Long id;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private String cellIdentifier;

    private CropTypeDTO cropType;

    private FarmerDTO farmer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCellIdentifier() {
        return cellIdentifier;
    }

    public void setCellIdentifier(String cellIdentifier) {
        this.cellIdentifier = cellIdentifier;
    }

    public CropTypeDTO getCropType() {
        return cropType;
    }

    public void setCropType(CropTypeDTO cropType) {
        this.cropType = cropType;
    }

    public FarmerDTO getFarmer() {
        return farmer;
    }

    public void setFarmer(FarmerDTO farmer) {
        this.farmer = farmer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FarmDTO)) {
            return false;
        }

        FarmDTO farmDTO = (FarmDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, farmDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FarmDTO{" +
            "id=" + getId() +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", cellIdentifier='" + getCellIdentifier() + "'" +
            ", cropType=" + getCropType() +
            ", farmer=" + getFarmer() +
            "}";
    }
}
