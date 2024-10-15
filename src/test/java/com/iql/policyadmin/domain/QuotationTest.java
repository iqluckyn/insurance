package com.iql.policyadmin.domain;

import static com.iql.policyadmin.domain.BusinessTestSamples.*;
import static com.iql.policyadmin.domain.FarmerTestSamples.*;
import static com.iql.policyadmin.domain.InsuredPolicyTestSamples.*;
import static com.iql.policyadmin.domain.ProductTestSamples.*;
import static com.iql.policyadmin.domain.QuotationStatusTestSamples.*;
import static com.iql.policyadmin.domain.QuotationTestSamples.*;
import static com.iql.policyadmin.domain.SeasonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class QuotationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quotation.class);
        Quotation quotation1 = getQuotationSample1();
        Quotation quotation2 = new Quotation();
        assertThat(quotation1).isNotEqualTo(quotation2);

        quotation2.setId(quotation1.getId());
        assertThat(quotation1).isEqualTo(quotation2);

        quotation2 = getQuotationSample2();
        assertThat(quotation1).isNotEqualTo(quotation2);
    }

    @Test
    void insuredPolicyTest() {
        Quotation quotation = getQuotationRandomSampleGenerator();
        InsuredPolicy insuredPolicyBack = getInsuredPolicyRandomSampleGenerator();

        quotation.addInsuredPolicy(insuredPolicyBack);
        assertThat(quotation.getInsuredPolicies()).containsOnly(insuredPolicyBack);
        assertThat(insuredPolicyBack.getQuotation()).isEqualTo(quotation);

        quotation.removeInsuredPolicy(insuredPolicyBack);
        assertThat(quotation.getInsuredPolicies()).doesNotContain(insuredPolicyBack);
        assertThat(insuredPolicyBack.getQuotation()).isNull();

        quotation.insuredPolicies(new HashSet<>(Set.of(insuredPolicyBack)));
        assertThat(quotation.getInsuredPolicies()).containsOnly(insuredPolicyBack);
        assertThat(insuredPolicyBack.getQuotation()).isEqualTo(quotation);

        quotation.setInsuredPolicies(new HashSet<>());
        assertThat(quotation.getInsuredPolicies()).doesNotContain(insuredPolicyBack);
        assertThat(insuredPolicyBack.getQuotation()).isNull();
    }

    @Test
    void seasonTest() {
        Quotation quotation = getQuotationRandomSampleGenerator();
        Season seasonBack = getSeasonRandomSampleGenerator();

        quotation.setSeason(seasonBack);
        assertThat(quotation.getSeason()).isEqualTo(seasonBack);

        quotation.season(null);
        assertThat(quotation.getSeason()).isNull();
    }

    @Test
    void farmerTest() {
        Quotation quotation = getQuotationRandomSampleGenerator();
        Farmer farmerBack = getFarmerRandomSampleGenerator();

        quotation.setFarmer(farmerBack);
        assertThat(quotation.getFarmer()).isEqualTo(farmerBack);

        quotation.farmer(null);
        assertThat(quotation.getFarmer()).isNull();
    }

    @Test
    void productTest() {
        Quotation quotation = getQuotationRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        quotation.setProduct(productBack);
        assertThat(quotation.getProduct()).isEqualTo(productBack);

        quotation.product(null);
        assertThat(quotation.getProduct()).isNull();
    }

    @Test
    void businessTest() {
        Quotation quotation = getQuotationRandomSampleGenerator();
        Business businessBack = getBusinessRandomSampleGenerator();

        quotation.setBusiness(businessBack);
        assertThat(quotation.getBusiness()).isEqualTo(businessBack);

        quotation.business(null);
        assertThat(quotation.getBusiness()).isNull();
    }

    @Test
    void quotationStatusTest() {
        Quotation quotation = getQuotationRandomSampleGenerator();
        QuotationStatus quotationStatusBack = getQuotationStatusRandomSampleGenerator();

        quotation.setQuotationStatus(quotationStatusBack);
        assertThat(quotation.getQuotationStatus()).isEqualTo(quotationStatusBack);

        quotation.quotationStatus(null);
        assertThat(quotation.getQuotationStatus()).isNull();
    }
}
