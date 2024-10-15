package com.iql.policyadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A BusinessType.
 */
@Entity
@Table(name = "business_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusinessType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "businessType")
    @JsonIgnoreProperties(value = { "farmers", "quotations", "businessType" }, allowSetters = true)
    private Set<Business> businesses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BusinessType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public BusinessType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public BusinessType description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Business> getBusinesses() {
        return this.businesses;
    }

    public void setBusinesses(Set<Business> businesses) {
        if (this.businesses != null) {
            this.businesses.forEach(i -> i.setBusinessType(null));
        }
        if (businesses != null) {
            businesses.forEach(i -> i.setBusinessType(this));
        }
        this.businesses = businesses;
    }

    public BusinessType businesses(Set<Business> businesses) {
        this.setBusinesses(businesses);
        return this;
    }

    public BusinessType addBusiness(Business business) {
        this.businesses.add(business);
        business.setBusinessType(this);
        return this;
    }

    public BusinessType removeBusiness(Business business) {
        this.businesses.remove(business);
        business.setBusinessType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessType)) {
            return false;
        }
        return getId() != null && getId().equals(((BusinessType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
