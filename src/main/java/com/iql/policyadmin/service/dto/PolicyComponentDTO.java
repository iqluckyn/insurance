package com.iql.policyadmin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.iql.policyadmin.domain.PolicyComponent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PolicyComponentDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal componentValue;

    private Instant createdAt;

    private Instant updatedAt;

    private PComponentDTO component;

    private Set<InsuredPolicyDTO> policies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getComponentValue() {
        return componentValue;
    }

    public void setComponentValue(BigDecimal componentValue) {
        this.componentValue = componentValue;
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

    public PComponentDTO getComponent() {
        return component;
    }

    public void setComponent(PComponentDTO component) {
        this.component = component;
    }

    public Set<InsuredPolicyDTO> getPolicies() {
        return policies;
    }

    public void setPolicies(Set<InsuredPolicyDTO> policies) {
        this.policies = policies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PolicyComponentDTO)) {
            return false;
        }

        PolicyComponentDTO policyComponentDTO = (PolicyComponentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, policyComponentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PolicyComponentDTO{" +
            "id=" + getId() +
            ", componentValue=" + getComponentValue() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", component=" + getComponent() +
            ", policies=" + getPolicies() +
            "}";
    }
}
