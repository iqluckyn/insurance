package com.iql.policyadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A QuotationStatus.
 */
@Entity
@Table(name = "quotation_status")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "status_name", nullable = false)
    private String statusName;

    @NotNull
    @Column(name = "status_code", nullable = false)
    private String statusCode;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quotationStatus")
    @JsonIgnoreProperties(value = { "insuredPolicies", "season", "farmer", "product", "business", "quotationStatus" }, allowSetters = true)
    private Set<Quotation> quotations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuotationStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public QuotationStatus statusName(String statusName) {
        this.setStatusName(statusName);
        return this;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusCode() {
        return this.statusCode;
    }

    public QuotationStatus statusCode(String statusCode) {
        this.setStatusCode(statusCode);
        return this;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return this.description;
    }

    public QuotationStatus description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public QuotationStatus isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Quotation> getQuotations() {
        return this.quotations;
    }

    public void setQuotations(Set<Quotation> quotations) {
        if (this.quotations != null) {
            this.quotations.forEach(i -> i.setQuotationStatus(null));
        }
        if (quotations != null) {
            quotations.forEach(i -> i.setQuotationStatus(this));
        }
        this.quotations = quotations;
    }

    public QuotationStatus quotations(Set<Quotation> quotations) {
        this.setQuotations(quotations);
        return this;
    }

    public QuotationStatus addQuotations(Quotation quotation) {
        this.quotations.add(quotation);
        quotation.setQuotationStatus(this);
        return this;
    }

    public QuotationStatus removeQuotations(Quotation quotation) {
        this.quotations.remove(quotation);
        quotation.setQuotationStatus(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuotationStatus)) {
            return false;
        }
        return getId() != null && getId().equals(((QuotationStatus) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotationStatus{" +
            "id=" + getId() +
            ", statusName='" + getStatusName() + "'" +
            ", statusCode='" + getStatusCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
