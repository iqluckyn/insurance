package com.iql.policyadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Farm.
 */
@Entity
@Table(name = "farm")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Farm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @NotNull
    @Column(name = "cell_identifier", nullable = false)
    private String cellIdentifier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "seasons" }, allowSetters = true)
    private CropType cropType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "farms", "user", "business" }, allowSetters = true)
    private Farmer farmer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Farm id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Farm latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Farm longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCellIdentifier() {
        return this.cellIdentifier;
    }

    public Farm cellIdentifier(String cellIdentifier) {
        this.setCellIdentifier(cellIdentifier);
        return this;
    }

    public void setCellIdentifier(String cellIdentifier) {
        this.cellIdentifier = cellIdentifier;
    }

    public CropType getCropType() {
        return this.cropType;
    }

    public void setCropType(CropType cropType) {
        this.cropType = cropType;
    }

    public Farm cropType(CropType cropType) {
        this.setCropType(cropType);
        return this;
    }

    public Farmer getFarmer() {
        return this.farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public Farm farmer(Farmer farmer) {
        this.setFarmer(farmer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Farm)) {
            return false;
        }
        return getId() != null && getId().equals(((Farm) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Farm{" +
            "id=" + getId() +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", cellIdentifier='" + getCellIdentifier() + "'" +
            "}";
    }
}
