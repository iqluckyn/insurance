package com.iql.policyadmin.service.dto;

import com.iql.policyadmin.domain.enumeration.PolicyStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.iql.policyadmin.domain.InsuredPolicy} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsuredPolicyDTO implements Serializable {

    private Long id;

    @NotNull
    private String policyNumber;

    @NotNull
    private Instant startDate;

    @NotNull
    private Instant endDate;

    @NotNull
    private BigDecimal premiumAmount;

    @NotNull
    private BigDecimal coverageAmount;

    @NotNull
    private PolicyStatus status;

    private FarmerDTO insuredFarmer;

    private FarmDTO farm;

    private Set<PolicyComponentDTO> components = new HashSet<>();

    private QuotationDTO quotation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public BigDecimal getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(BigDecimal coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public void setStatus(PolicyStatus status) {
        this.status = status;
    }

    public FarmerDTO getInsuredFarmer() {
        return insuredFarmer;
    }

    public void setInsuredFarmer(FarmerDTO insuredFarmer) {
        this.insuredFarmer = insuredFarmer;
    }

    public FarmDTO getFarm() {
        return farm;
    }

    public void setFarm(FarmDTO farm) {
        this.farm = farm;
    }

    public Set<PolicyComponentDTO> getComponents() {
        return components;
    }

    public void setComponents(Set<PolicyComponentDTO> components) {
        this.components = components;
    }

    public QuotationDTO getQuotation() {
        return quotation;
    }

    public void setQuotation(QuotationDTO quotation) {
        this.quotation = quotation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsuredPolicyDTO)) {
            return false;
        }

        InsuredPolicyDTO insuredPolicyDTO = (InsuredPolicyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, insuredPolicyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuredPolicyDTO{" +
            "id=" + getId() +
            ", policyNumber='" + getPolicyNumber() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", premiumAmount=" + getPremiumAmount() +
            ", coverageAmount=" + getCoverageAmount() +
            ", status='" + getStatus() + "'" +
            ", insuredFarmer=" + getInsuredFarmer() +
            ", farm=" + getFarm() +
            ", components=" + getComponents() +
            ", quotation=" + getQuotation() +
            "}";
    }
}
