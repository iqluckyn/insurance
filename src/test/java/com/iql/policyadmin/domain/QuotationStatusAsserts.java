package com.iql.policyadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class QuotationStatusAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertQuotationStatusAllPropertiesEquals(QuotationStatus expected, QuotationStatus actual) {
        assertQuotationStatusAutoGeneratedPropertiesEquals(expected, actual);
        assertQuotationStatusAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertQuotationStatusAllUpdatablePropertiesEquals(QuotationStatus expected, QuotationStatus actual) {
        assertQuotationStatusUpdatableFieldsEquals(expected, actual);
        assertQuotationStatusUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertQuotationStatusAutoGeneratedPropertiesEquals(QuotationStatus expected, QuotationStatus actual) {
        assertThat(expected)
            .as("Verify QuotationStatus auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertQuotationStatusUpdatableFieldsEquals(QuotationStatus expected, QuotationStatus actual) {
        assertThat(expected)
            .as("Verify QuotationStatus relevant properties")
            .satisfies(e -> assertThat(e.getStatusName()).as("check statusName").isEqualTo(actual.getStatusName()))
            .satisfies(e -> assertThat(e.getStatusCode()).as("check statusCode").isEqualTo(actual.getStatusCode()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getIsActive()).as("check isActive").isEqualTo(actual.getIsActive()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertQuotationStatusUpdatableRelationshipsEquals(QuotationStatus expected, QuotationStatus actual) {
        // empty method
    }
}
