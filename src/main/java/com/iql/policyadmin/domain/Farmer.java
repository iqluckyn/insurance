package com.iql.policyadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Farmer.
 */
@Entity
@Table(name = "farmer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Farmer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "province")
    private String province;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "registration_date")
    private Instant registrationDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "farmer")
    @JsonIgnoreProperties(value = { "cropType", "farmer" }, allowSetters = true)
    private Set<Farm> farms = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "farmers", "quotations", "businessType" }, allowSetters = true)
    private Business business;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Farmer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public Farmer firstname(String firstname) {
        this.setFirstname(firstname);
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public Farmer lastname(String lastname) {
        this.setLastname(lastname);
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return this.email;
    }

    public Farmer email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return this.position;
    }

    public Farmer position(String position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return this.phone;
    }

    public Farmer phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public Farmer address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public Farmer city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return this.province;
    }

    public Farmer province(String province) {
        this.setProvince(province);
        return this;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return this.country;
    }

    public Farmer country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Farmer postalCode(String postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Instant getRegistrationDate() {
        return this.registrationDate;
    }

    public Farmer registrationDate(Instant registrationDate) {
        this.setRegistrationDate(registrationDate);
        return this;
    }

    public void setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Set<Farm> getFarms() {
        return this.farms;
    }

    public void setFarms(Set<Farm> farms) {
        if (this.farms != null) {
            this.farms.forEach(i -> i.setFarmer(null));
        }
        if (farms != null) {
            farms.forEach(i -> i.setFarmer(this));
        }
        this.farms = farms;
    }

    public Farmer farms(Set<Farm> farms) {
        this.setFarms(farms);
        return this;
    }

    public Farmer addFarm(Farm farm) {
        this.farms.add(farm);
        farm.setFarmer(this);
        return this;
    }

    public Farmer removeFarm(Farm farm) {
        this.farms.remove(farm);
        farm.setFarmer(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Farmer user(User user) {
        this.setUser(user);
        return this;
    }

    public Business getBusiness() {
        return this.business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Farmer business(Business business) {
        this.setBusiness(business);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Farmer)) {
            return false;
        }
        return getId() != null && getId().equals(((Farmer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Farmer{" +
            "id=" + getId() +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", email='" + getEmail() + "'" +
            ", position='" + getPosition() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", province='" + getProvince() + "'" +
            ", country='" + getCountry() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            "}";
    }
}
