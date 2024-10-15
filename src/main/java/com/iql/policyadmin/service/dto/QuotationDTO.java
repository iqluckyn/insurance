package com.iql.policyadmin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.iql.policyadmin.domain.Quotation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate startOfRiskPeriod;

    @NotNull
    private Integer lengthOfRiskPeriod;

    @NotNull
    private Integer depth;

    @NotNull
    private Integer claimsFrequency;

    @NotNull
    private BigDecimal insuredValue;

    @NotNull
    private BigDecimal bestPremium;

    @NotNull
    private BigDecimal insuredRate;

    @NotNull
    private BigDecimal insuredPremium;

    private Instant createdAt;

    private Instant updatedAt;

    private SeasonDTO season;

    private FarmerDTO farmer;

    private ProductDTO product;

    private BusinessDTO business;

    private QuotationStatusDTO quotationStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartOfRiskPeriod() {
        return startOfRiskPeriod;
    }

    public void setStartOfRiskPeriod(LocalDate startOfRiskPeriod) {
        this.startOfRiskPeriod = startOfRiskPeriod;
    }

    public Integer getLengthOfRiskPeriod() {
        return lengthOfRiskPeriod;
    }

    public void setLengthOfRiskPeriod(Integer lengthOfRiskPeriod) {
        this.lengthOfRiskPeriod = lengthOfRiskPeriod;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Integer getClaimsFrequency() {
        return claimsFrequency;
    }

    public void setClaimsFrequency(Integer claimsFrequency) {
        this.claimsFrequency = claimsFrequency;
    }

    public BigDecimal getInsuredValue() {
        return insuredValue;
    }

    public void setInsuredValue(BigDecimal insuredValue) {
        this.insuredValue = insuredValue;
    }

    public BigDecimal getBestPremium() {
        return bestPremium;
    }

    public void setBestPremium(BigDecimal bestPremium) {
        this.bestPremium = bestPremium;
    }

    public BigDecimal getInsuredRate() {
        return insuredRate;
    }

    public void setInsuredRate(BigDecimal insuredRate) {
        this.insuredRate = insuredRate;
    }

    public BigDecimal getInsuredPremium() {
        return insuredPremium;
    }

    public void setInsuredPremium(BigDecimal insuredPremium) {
        this.insuredPremium = insuredPremium;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SeasonDTO getSeason() {
        return season;
    }

    public void setSeason(SeasonDTO season) {
        this.season = season;
    }

    public FarmerDTO getFarmer() {
        return farmer;
    }

    public void setFarmer(FarmerDTO farmer) {
        this.farmer = farmer;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public BusinessDTO getBusiness() {
        return business;
    }

    public void setBusiness(BusinessDTO business) {
        this.business = business;
    }

    public QuotationStatusDTO getQuotationStatus() {
        return quotationStatus;
    }

    public void setQuotationStatus(QuotationStatusDTO quotationStatus) {
        this.quotationStatus = quotationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuotationDTO)) {
            return false;
        }

        QuotationDTO quotationDTO = (QuotationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quotationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotationDTO{" +
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
            ", season=" + getSeason() +
            ", farmer=" + getFarmer() +
            ", product=" + getProduct() +
            ", business=" + getBusiness() +
            ", quotationStatus=" + getQuotationStatus() +
            "}";
    }
}
