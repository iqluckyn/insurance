package com.iql.policyadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Season.
 */
@Entity
@Table(name = "season")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Season implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "season_name", nullable = false)
    private String seasonName;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "seasons" }, allowSetters = true)
    private CropType cropType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Season id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeasonName() {
        return this.seasonName;
    }

    public Season seasonName(String seasonName) {
        this.setSeasonName(seasonName);
        return this;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Season startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Season endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Season isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public CropType getCropType() {
        return this.cropType;
    }

    public void setCropType(CropType cropType) {
        this.cropType = cropType;
    }

    public Season cropType(CropType cropType) {
        this.setCropType(cropType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Season)) {
            return false;
        }
        return getId() != null && getId().equals(((Season) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Season{" +
            "id=" + getId() +
            ", seasonName='" + getSeasonName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
