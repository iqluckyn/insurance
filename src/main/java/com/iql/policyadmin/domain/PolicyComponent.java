package com.iql.policyadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A PolicyComponent.
 */
@Entity
@Table(name = "policy_component")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PolicyComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "component_value", precision = 21, scale = 2, nullable = false)
    private BigDecimal componentValue;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "policyComponents" }, allowSetters = true)
    private PComponent component;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "components")
    @JsonIgnoreProperties(value = { "insuredFarmer", "farm", "components", "quotation" }, allowSetters = true)
    private Set<InsuredPolicy> policies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PolicyComponent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getComponentValue() {
        return this.componentValue;
    }

    public PolicyComponent componentValue(BigDecimal componentValue) {
        this.setComponentValue(componentValue);
        return this;
    }

    public void setComponentValue(BigDecimal componentValue) {
        this.componentValue = componentValue;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public PolicyComponent createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public PolicyComponent updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PComponent getComponent() {
        return this.component;
    }

    public void setComponent(PComponent pComponent) {
        this.component = pComponent;
    }

    public PolicyComponent component(PComponent pComponent) {
        this.setComponent(pComponent);
        return this;
    }

    public Set<InsuredPolicy> getPolicies() {
        return this.policies;
    }

    public void setPolicies(Set<InsuredPolicy> insuredPolicies) {
        if (this.policies != null) {
            this.policies.forEach(i -> i.removeComponents(this));
        }
        if (insuredPolicies != null) {
            insuredPolicies.forEach(i -> i.addComponents(this));
        }
        this.policies = insuredPolicies;
    }

    public PolicyComponent policies(Set<InsuredPolicy> insuredPolicies) {
        this.setPolicies(insuredPolicies);
        return this;
    }

    public PolicyComponent addPolicies(InsuredPolicy insuredPolicy) {
        this.policies.add(insuredPolicy);
        insuredPolicy.getComponents().add(this);
        return this;
    }

    public PolicyComponent removePolicies(InsuredPolicy insuredPolicy) {
        this.policies.remove(insuredPolicy);
        insuredPolicy.getComponents().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PolicyComponent)) {
            return false;
        }
        return getId() != null && getId().equals(((PolicyComponent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PolicyComponent{" +
            "id=" + getId() +
            ", componentValue=" + getComponentValue() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
