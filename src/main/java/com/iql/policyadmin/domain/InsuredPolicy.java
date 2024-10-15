package com.iql.policyadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iql.policyadmin.domain.enumeration.PolicyStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A InsuredPolicy.
 */
@Entity
@Table(name = "insured_policy")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsuredPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "policy_number", nullable = false)
    private String policyNumber;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    @NotNull
    @Column(name = "premium_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal premiumAmount;

    @NotNull
    @Column(name = "coverage_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal coverageAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PolicyStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "farms", "user", "business" }, allowSetters = true)
    private Farmer insuredFarmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cropType", "farmer" }, allowSetters = true)
    private Farm farm;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_insured_policy__components",
        joinColumns = @JoinColumn(name = "insured_policy_id"),
        inverseJoinColumns = @JoinColumn(name = "components_id")
    )
    @JsonIgnoreProperties(value = { "component", "policies" }, allowSetters = true)
    private Set<PolicyComponent> components = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "insuredPolicies", "season", "farmer", "product", "business", "quotationStatus" }, allowSetters = true)
    private Quotation quotation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InsuredPolicy id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolicyNumber() {
        return this.policyNumber;
    }

    public InsuredPolicy policyNumber(String policyNumber) {
        this.setPolicyNumber(policyNumber);
        return this;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public InsuredPolicy startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public InsuredPolicy endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPremiumAmount() {
        return this.premiumAmount;
    }

    public InsuredPolicy premiumAmount(BigDecimal premiumAmount) {
        this.setPremiumAmount(premiumAmount);
        return this;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public BigDecimal getCoverageAmount() {
        return this.coverageAmount;
    }

    public InsuredPolicy coverageAmount(BigDecimal coverageAmount) {
        this.setCoverageAmount(coverageAmount);
        return this;
    }

    public void setCoverageAmount(BigDecimal coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public PolicyStatus getStatus() {
        return this.status;
    }

    public InsuredPolicy status(PolicyStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PolicyStatus status) {
        this.status = status;
    }

    public Farmer getInsuredFarmer() {
        return this.insuredFarmer;
    }

    public void setInsuredFarmer(Farmer farmer) {
        this.insuredFarmer = farmer;
    }

    public InsuredPolicy insuredFarmer(Farmer farmer) {
        this.setInsuredFarmer(farmer);
        return this;
    }

    public Farm getFarm() {
        return this.farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public InsuredPolicy farm(Farm farm) {
        this.setFarm(farm);
        return this;
    }

    public Set<PolicyComponent> getComponents() {
        return this.components;
    }

    public void setComponents(Set<PolicyComponent> policyComponents) {
        this.components = policyComponents;
    }

    public InsuredPolicy components(Set<PolicyComponent> policyComponents) {
        this.setComponents(policyComponents);
        return this;
    }

    public InsuredPolicy addComponents(PolicyComponent policyComponent) {
        this.components.add(policyComponent);
        return this;
    }

    public InsuredPolicy removeComponents(PolicyComponent policyComponent) {
        this.components.remove(policyComponent);
        return this;
    }

    public Quotation getQuotation() {
        return this.quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    public InsuredPolicy quotation(Quotation quotation) {
        this.setQuotation(quotation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsuredPolicy)) {
            return false;
        }
        return getId() != null && getId().equals(((InsuredPolicy) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuredPolicy{" +
            "id=" + getId() +
            ", policyNumber='" + getPolicyNumber() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", premiumAmount=" + getPremiumAmount() +
            ", coverageAmount=" + getCoverageAmount() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
