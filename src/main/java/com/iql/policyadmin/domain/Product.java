package com.iql.policyadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnoreProperties(value = { "insuredPolicies", "season", "farmer", "product", "business", "quotationStatus" }, allowSetters = true)
    private Set<Quotation> quotations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return this.productName;
    }

    public Product productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public Product productCode(String productCode) {
        this.setProductCode(productCode);
        return this;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductDescription() {
        return this.productDescription;
    }

    public Product productDescription(String productDescription) {
        this.setProductDescription(productDescription);
        return this;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Product isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Product createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Product updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Quotation> getQuotations() {
        return this.quotations;
    }

    public void setQuotations(Set<Quotation> quotations) {
        if (this.quotations != null) {
            this.quotations.forEach(i -> i.setProduct(null));
        }
        if (quotations != null) {
            quotations.forEach(i -> i.setProduct(this));
        }
        this.quotations = quotations;
    }

    public Product quotations(Set<Quotation> quotations) {
        this.setQuotations(quotations);
        return this;
    }

    public Product addQuotation(Quotation quotation) {
        this.quotations.add(quotation);
        quotation.setProduct(this);
        return this;
    }

    public Product removeQuotation(Quotation quotation) {
        this.quotations.remove(quotation);
        quotation.setProduct(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", productCode='" + getProductCode() + "'" +
            ", productDescription='" + getProductDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
