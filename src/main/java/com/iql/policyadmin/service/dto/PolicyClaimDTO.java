package com.iql.policyadmin.service.dto;

import com.iql.policyadmin.domain.enumeration.ClaimStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.iql.policyadmin.domain.PolicyClaim} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PolicyClaimDTO implements Serializable {

    private Long id;

    @NotNull
    private String claimNumber;

    @NotNull
    private Instant claimDate;

    @NotNull
    private BigDecimal amountClaimed;

    @NotNull
    private ClaimStatus status;

    private InsuredPolicyDTO policy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public Instant getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Instant claimDate) {
        this.claimDate = claimDate;
    }

    public BigDecimal getAmountClaimed() {
        return amountClaimed;
    }

    public void setAmountClaimed(BigDecimal amountClaimed) {
        this.amountClaimed = amountClaimed;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public InsuredPolicyDTO getPolicy() {
        return policy;
    }

    public void setPolicy(InsuredPolicyDTO policy) {
        this.policy = policy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PolicyClaimDTO)) {
            return false;
        }

        PolicyClaimDTO policyClaimDTO = (PolicyClaimDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, policyClaimDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PolicyClaimDTO{" +
            "id=" + getId() +
            ", claimNumber='" + getClaimNumber() + "'" +
            ", claimDate='" + getClaimDate() + "'" +
            ", amountClaimed=" + getAmountClaimed() +
            ", status='" + getStatus() + "'" +
            ", policy=" + getPolicy() +
            "}";
    }
}
