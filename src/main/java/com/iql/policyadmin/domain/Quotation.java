package com.iql.policyadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Quotation.
 */
@Entity
@Table(name = "quotation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quotation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_of_risk_period", nullable = false)
    private LocalDate startOfRiskPeriod;

    @NotNull
    @Column(name = "length_of_risk_period", nullable = false)
    private Integer lengthOfRiskPeriod;

    @NotNull
    @Column(name = "depth", nullable = false)
    private Integer depth;

    @NotNull
    @Column(name = "claims_frequency", nullable = false)
    private Integer claimsFrequency;

    @NotNull
    @Column(name = "insured_value", precision = 21, scale = 2, nullable = false)
    private BigDecimal insuredValue;

    @NotNull
    @Column(name = "best_premium", precision = 21, scale = 2, nullable = false)
    private BigDecimal bestPremium;

    @NotNull
    @Column(name = "insured_rate", precision = 21, scale = 2, nullable = false)
    private BigDecimal insuredRate;

    @NotNull
    @Column(name = "insured_premium", precision = 21, scale = 2, nullable = false)
    private BigDecimal insuredPremium;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quotation")
    @JsonIgnoreProperties(value = { "insuredFarmer", "farm", "components", "quotation" }, allowSetters = true)
    private Set<InsuredPolicy> insuredPolicies = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cropType" }, allowSetters = true)
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "farms", "user", "business" }, allowSetters = true)
    private Farmer farmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "quotations" }, allowSetters = true)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "farmers", "quotations", "businessType" }, allowSetters = true)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "quotations" }, allowSetters = true)
    private QuotationStatus quotationStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Quotation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartOfRiskPeriod() {
        return this.startOfRiskPeriod;
    }

    public Quotation startOfRiskPeriod(LocalDate startOfRiskPeriod) {
        this.setStartOfRiskPeriod(startOfRiskPeriod);
        return this;
    }

    public void setStartOfRiskPeriod(LocalDate startOfRiskPeriod) {
        this.startOfRiskPeriod = startOfRiskPeriod;
    }

    public Integer getLengthOfRiskPeriod() {
        return this.lengthOfRiskPeriod;
    }

    public Quotation lengthOfRiskPeriod(Integer lengthOfRiskPeriod) {
        this.setLengthOfRiskPeriod(lengthOfRiskPeriod);
        return this;
    }

    public void setLengthOfRiskPeriod(Integer lengthOfRiskPeriod) {
        this.lengthOfRiskPeriod = lengthOfRiskPeriod;
    }

    public Integer getDepth() {
        return this.depth;
    }

    public Quotation depth(Integer depth) {
        this.setDepth(depth);
        return this;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Integer getClaimsFrequency() {
        return this.claimsFrequency;
    }

    public Quotation claimsFrequency(Integer claimsFrequency) {
        this.setClaimsFrequency(claimsFrequency);
        return this;
    }

    public void setClaimsFrequency(Integer claimsFrequency) {
        this.claimsFrequency = claimsFrequency;
    }

    public BigDecimal getInsuredValue() {
        return this.insuredValue;
    }

    public Quotation insuredValue(BigDecimal insuredValue) {
        this.setInsuredValue(insuredValue);
        return this;
    }

    public void setInsuredValue(BigDecimal insuredValue) {
        this.insuredValue = insuredValue;
    }

    public BigDecimal getBestPremium() {
        return this.bestPremium;
    }

    public Quotation bestPremium(BigDecimal bestPremium) {
        this.setBestPremium(bestPremium);
        return this;
    }

    public void setBestPremium(BigDecimal bestPremium) {
        this.bestPremium = bestPremium;
    }

    public BigDecimal getInsuredRate() {
        return this.insuredRate;
    }

    public Quotation insuredRate(BigDecimal insuredRate) {
        this.setInsuredRate(insuredRate);
        return this;
    }

    public void setInsuredRate(BigDecimal insuredRate) {
        this.insuredRate = insuredRate;
    }

    public BigDecimal getInsuredPremium() {
        return this.insuredPremium;
    }

    public Quotation insuredPremium(BigDecimal insuredPremium) {
        this.setInsuredPremium(insuredPremium);
        return this;
    }

    public void setInsuredPremium(BigDecimal insuredPremium) {
        this.insuredPremium = insuredPremium;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Quotation createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Quotation updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<InsuredPolicy> getInsuredPolicies() {
        return this.insuredPolicies;
    }

    public void setInsuredPolicies(Set<InsuredPolicy> insuredPolicies) {
        if (this.insuredPolicies != null) {
            this.insuredPolicies.forEach(i -> i.setQuotation(null));
        }
        if (insuredPolicies != null) {
            insuredPolicies.forEach(i -> i.setQuotation(this));
        }
        this.insuredPolicies = insuredPolicies;
    }

    public Quotation insuredPolicies(Set<InsuredPolicy> insuredPolicies) {
        this.setInsuredPolicies(insuredPolicies);
        return this;
    }

    public Quotation addInsuredPolicy(InsuredPolicy insuredPolicy) {
        this.insuredPolicies.add(insuredPolicy);
        insuredPolicy.setQuotation(this);
        return this;
    }

    public Quotation removeInsuredPolicy(InsuredPolicy insuredPolicy) {
        this.insuredPolicies.remove(insuredPolicy);
        insuredPolicy.setQuotation(null);
        return this;
    }

    public Season getSeason() {
        return this.season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Quotation season(Season season) {
        this.setSeason(season);
        return this;
    }

    public Farmer getFarmer() {
        return this.farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public Quotation farmer(Farmer farmer) {
        this.setFarmer(farmer);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Quotation product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Business getBusiness() {
        return this.business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Quotation business(Business business) {
        this.setBusiness(business);
        return this;
    }

    public QuotationStatus getQuotationStatus() {
        return this.quotationStatus;
    }

    public void setQuotationStatus(QuotationStatus quotationStatus) {
        this.quotationStatus = quotationStatus;
    }

    public Quotation quotationStatus(QuotationStatus quotationStatus) {
        this.setQuotationStatus(quotationStatus);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quotation)) {
            return false;
        }
        return getId() != null && getId().equals(((Quotation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quotation{" +
            "id=" + getId() +
            ", startOfRiskPeriod='" + getStartOfRiskPeriod() + "'" +
            ", lengthOfRiskPeriod=" + getLengthOfRiskPeriod() +
            ", depth=" + getDepth() +
            ", claimsFrequency=" + getClaimsFrequency() +
            ", insuredValue=" + getInsuredValue() +
            ", bestPremium=" + getBestPremium() +
            ", insuredRate=" + getInsuredRate() +
            ", insuredPremium=" + getInsuredPremium() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
