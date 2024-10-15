package com.iql.policyadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A CropType.
 */
@Entity
@Table(name = "crop_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CropType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "crop_name", nullable = false)
    private String cropName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cropType")
    @JsonIgnoreProperties(value = { "cropType" }, allowSetters = true)
    private Set<Season> seasons = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CropType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCropName() {
        return this.cropName;
    }

    public CropType cropName(String cropName) {
        this.setCropName(cropName);
        return this;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public Set<Season> getSeasons() {
        return this.seasons;
    }

    public void setSeasons(Set<Season> seasons) {
        if (this.seasons != null) {
            this.seasons.forEach(i -> i.setCropType(null));
        }
        if (seasons != null) {
            seasons.forEach(i -> i.setCropType(this));
        }
        this.seasons = seasons;
    }

    public CropType seasons(Set<Season> seasons) {
        this.setSeasons(seasons);
        return this;
    }

    public CropType addSeason(Season season) {
        this.seasons.add(season);
        season.setCropType(this);
        return this;
    }

    public CropType removeSeason(Season season) {
        this.seasons.remove(season);
        season.setCropType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CropType)) {
            return false;
        }
        return getId() != null && getId().equals(((CropType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CropType{" +
            "id=" + getId() +
            ", cropName='" + getCropName() + "'" +
            "}";
    }
}
