package com.iql.policyadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Business.
 */
@Entity
@Table(name = "business")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Business implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "registered_name", nullable = false)
    private String registeredName;

    @Column(name = "organisation_name")
    private String organisationName;

    @Column(name = "vat_number")
    private String vatNumber;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "business")
    @JsonIgnoreProperties(value = { "farms", "user", "business" }, allowSetters = true)
    private Set<Farmer> farmers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "business")
    @JsonIgnoreProperties(value = { "insuredPolicies", "season", "farmer", "product", "business", "quotationStatus" }, allowSetters = true)
    private Set<Quotation> quotations = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "businesses" }, allowSetters = true)
    private BusinessType businessType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Business id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegisteredName() {
        return this.registeredName;
    }

    public Business registeredName(String registeredName) {
        this.setRegisteredName(registeredName);
        return this;
    }

    public void setRegisteredName(String registeredName) {
        this.registeredName = registeredName;
    }

    public String getOrganisationName() {
        return this.organisationName;
    }

    public Business organisationName(String organisationName) {
        this.setOrganisationName(organisationName);
        return this;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getVatNumber() {
        return this.vatNumber;
    }

    public Business vatNumber(String vatNumber) {
        this.setVatNumber(vatNumber);
        return this;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Business createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Business updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Farmer> getFarmers() {
        return this.farmers;
    }

    public void setFarmers(Set<Farmer> farmers) {
        if (this.farmers != null) {
            this.farmers.forEach(i -> i.setBusiness(null));
        }
        if (farmers != null) {
            farmers.forEach(i -> i.setBusiness(this));
        }
        this.farmers = farmers;
    }

    public Business farmers(Set<Farmer> farmers) {
        this.setFarmers(farmers);
        return this;
    }

    public Business addFarmer(Farmer farmer) {
        this.farmers.add(farmer);
        farmer.setBusiness(this);
        return this;
    }

    public Business removeFarmer(Farmer farmer) {
        this.farmers.remove(farmer);
        farmer.setBusiness(null);
        return this;
    }

    public Set<Quotation> getQuotations() {
        return this.quotations;
    }

    public void setQuotations(Set<Quotation> quotations) {
        if (this.quotations != null) {
            this.quotations.forEach(i -> i.setBusiness(null));
        }
        if (quotations != null) {
            quotations.forEach(i -> i.setBusiness(this));
        }
        this.quotations = quotations;
    }

    public Business quotations(Set<Quotation> quotations) {
        this.setQuotations(quotations);
        return this;
    }

    public Business addQuotation(Quotation quotation) {
        this.quotations.add(quotation);
        quotation.setBusiness(this);
        return this;
    }

    public Business removeQuotation(Quotation quotation) {
        this.quotations.remove(quotation);
        quotation.setBusiness(null);
        return this;
    }

    public BusinessType getBusinessType() {
        return this.businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public Business businessType(BusinessType businessType) {
        this.setBusinessType(businessType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Business)) {
            return false;
        }
        return getId() != null && getId().equals(((Business) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Business{" +
            "id=" + getId() +
            ", registeredName='" + getRegisteredName() + "'" +
            ", organisationName='" + getOrganisationName() + "'" +
            ", vatNumber='" + getVatNumber() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
