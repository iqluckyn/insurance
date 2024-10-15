package com.iql.policyadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A PComponent.
 */
@Entity
@Table(name = "p_component")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "component")
    @JsonIgnoreProperties(value = { "component", "policies" }, allowSetters = true)
    private Set<PolicyComponent> policyComponents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PComponent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PComponent name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public PComponent description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<PolicyComponent> getPolicyComponents() {
        return this.policyComponents;
    }

    public void setPolicyComponents(Set<PolicyComponent> policyComponents) {
        if (this.policyComponents != null) {
            this.policyComponents.forEach(i -> i.setComponent(null));
        }
        if (policyComponents != null) {
            policyComponents.forEach(i -> i.setComponent(this));
        }
        this.policyComponents = policyComponents;
    }

    public PComponent policyComponents(Set<PolicyComponent> policyComponents) {
        this.setPolicyComponents(policyComponents);
        return this;
    }

    public PComponent addPolicyComponent(PolicyComponent policyComponent) {
        this.policyComponents.add(policyComponent);
        policyComponent.setComponent(this);
        return this;
    }

    public PComponent removePolicyComponent(PolicyComponent policyComponent) {
        this.policyComponents.remove(policyComponent);
        policyComponent.setComponent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PComponent)) {
            return false;
        }
        return getId() != null && getId().equals(((PComponent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PComponent{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
