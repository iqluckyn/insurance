package com.iql.policyadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class FarmerAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFarmerAllPropertiesEquals(Farmer expected, Farmer actual) {
        assertFarmerAutoGeneratedPropertiesEquals(expected, actual);
        assertFarmerAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFarmerAllUpdatablePropertiesEquals(Farmer expected, Farmer actual) {
        assertFarmerUpdatableFieldsEquals(expected, actual);
        assertFarmerUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFarmerAutoGeneratedPropertiesEquals(Farmer expected, Farmer actual) {
        assertThat(expected)
            .as("Verify Farmer auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFarmerUpdatableFieldsEquals(Farmer expected, Farmer actual) {
        assertThat(expected)
            .as("Verify Farmer relevant properties")
            .satisfies(e -> assertThat(e.getFirstname()).as("check firstname").isEqualTo(actual.getFirstname()))
            .satisfies(e -> assertThat(e.getLastname()).as("check lastname").isEqualTo(actual.getLastname()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getPosition()).as("check position").isEqualTo(actual.getPosition()))
            .satisfies(e -> assertThat(e.getPhone()).as("check phone").isEqualTo(actual.getPhone()))
            .satisfies(e -> assertThat(e.getAddress()).as("check address").isEqualTo(actual.getAddress()))
            .satisfies(e -> assertThat(e.getCity()).as("check city").isEqualTo(actual.getCity()))
            .satisfies(e -> assertThat(e.getProvince()).as("check province").isEqualTo(actual.getProvince()))
            .satisfies(e -> assertThat(e.getCountry()).as("check country").isEqualTo(actual.getCountry()))
            .satisfies(e -> assertThat(e.getPostalCode()).as("check postalCode").isEqualTo(actual.getPostalCode()))
            .satisfies(e -> assertThat(e.getRegistrationDate()).as("check registrationDate").isEqualTo(actual.getRegistrationDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFarmerUpdatableRelationshipsEquals(Farmer expected, Farmer actual) {
        assertThat(expected)
            .as("Verify Farmer relationships")
            .satisfies(e -> assertThat(e.getBusiness()).as("check business").isEqualTo(actual.getBusiness()));
    }
}
