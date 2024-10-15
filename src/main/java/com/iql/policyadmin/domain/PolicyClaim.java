package com.iql.policyadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iql.policyadmin.domain.enumeration.ClaimStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A PolicyClaim.
 */
@Entity
@Table(name = "policy_claim")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PolicyClaim implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "claim_number", nullable = false)
    private String claimNumber;

    @NotNull
    @Column(name = "claim_date", nullable = false)
    private Instant claimDate;

    @NotNull
    @Column(name = "amount_claimed", precision = 21, scale = 2, nullable = false)
    private BigDecimal amountClaimed;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ClaimStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "insuredFarmer", "farm", "components", "quotation" }, allowSetters = true)
    private InsuredPolicy policy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PolicyClaim id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClaimNumber() {
        return this.claimNumber;
    }

    public PolicyClaim claimNumber(String claimNumber) {
        this.setClaimNumber(claimNumber);
        return this;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public Instant getClaimDate() {
        return this.claimDate;
    }

    public PolicyClaim claimDate(Instant claimDate) {
        this.setClaimDate(claimDate);
        return this;
    }

    public void setClaimDate(Instant claimDate) {
        this.claimDate = claimDate;
    }

    public BigDecimal getAmountClaimed() {
        return this.amountClaimed;
    }

    public PolicyClaim amountClaimed(BigDecimal amountClaimed) {
        this.setAmountClaimed(amountClaimed);
        return this;
    }

    public void setAmountClaimed(BigDecimal amountClaimed) {
        this.amountClaimed = amountClaimed;
    }

    public ClaimStatus getStatus() {
        return this.status;
    }

    public PolicyClaim status(ClaimStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public InsuredPolicy getPolicy() {
        return this.policy;
    }

    public void setPolicy(InsuredPolicy insuredPolicy) {
        this.policy = insuredPolicy;
    }

    public PolicyClaim policy(InsuredPolicy insuredPolicy) {
        this.setPolicy(insuredPolicy);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PolicyClaim)) {
            return false;
        }
        return getId() != null && getId().equals(((PolicyClaim) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PolicyClaim{" +
            "id=" + getId() +
            ", claimNumber='" + getClaimNumber() + "'" +
            ", claimDate='" + getClaimDate() + "'" +
            ", amountClaimed=" + getAmountClaimed() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
