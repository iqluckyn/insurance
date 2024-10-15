package com.iql.policyadmin.domain;

import static com.iql.policyadmin.domain.FarmTestSamples.*;
import static com.iql.policyadmin.domain.FarmerTestSamples.*;
import static com.iql.policyadmin.domain.InsuredPolicyTestSamples.*;
import static com.iql.policyadmin.domain.PolicyComponentTestSamples.*;
import static com.iql.policyadmin.domain.QuotationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class InsuredPolicyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuredPolicy.class);
        InsuredPolicy insuredPolicy1 = getInsuredPolicySample1();
        InsuredPolicy insuredPolicy2 = new InsuredPolicy();
        assertThat(insuredPolicy1).isNotEqualTo(insuredPolicy2);

        insuredPolicy2.setId(insuredPolicy1.getId());
        assertThat(insuredPolicy1).isEqualTo(insuredPolicy2);

        insuredPolicy2 = getInsuredPolicySample2();
        assertThat(insuredPolicy1).isNotEqualTo(insuredPolicy2);
    }

    @Test
    void insuredFarmerTest() {
        InsuredPolicy insuredPolicy = getInsuredPolicyRandomSampleGenerator();
        Farmer farmerBack = getFarmerRandomSampleGenerator();

        insuredPolicy.setInsuredFarmer(farmerBack);
        assertThat(insuredPolicy.getInsuredFarmer()).isEqualTo(farmerBack);

        insuredPolicy.insuredFarmer(null);
        assertThat(insuredPolicy.getInsuredFarmer()).isNull();
    }

    @Test
    void farmTest() {
        InsuredPolicy insuredPolicy = getInsuredPolicyRandomSampleGenerator();
        Farm farmBack = getFarmRandomSampleGenerator();

        insuredPolicy.setFarm(farmBack);
        assertThat(insuredPolicy.getFarm()).isEqualTo(farmBack);

        insuredPolicy.farm(null);
        assertThat(insuredPolicy.getFarm()).isNull();
    }

    @Test
    void componentsTest() {
        InsuredPolicy insuredPolicy = getInsuredPolicyRandomSampleGenerator();
        PolicyComponent policyComponentBack = getPolicyComponentRandomSampleGenerator();

        insuredPolicy.addComponents(policyComponentBack);
        assertThat(insuredPolicy.getComponents()).containsOnly(policyComponentBack);

        insuredPolicy.removeComponents(policyComponentBack);
        assertThat(insuredPolicy.getComponents()).doesNotContain(policyComponentBack);

        insuredPolicy.components(new HashSet<>(Set.of(policyComponentBack)));
        assertThat(insuredPolicy.getComponents()).containsOnly(policyComponentBack);

        insuredPolicy.setComponents(new HashSet<>());
        assertThat(insuredPolicy.getComponents()).doesNotContain(policyComponentBack);
    }

    @Test
    void quotationTest() {
        InsuredPolicy insuredPolicy = getInsuredPolicyRandomSampleGenerator();
        Quotation quotationBack = getQuotationRandomSampleGenerator();

        insuredPolicy.setQuotation(quotationBack);
        assertThat(insuredPolicy.getQuotation()).isEqualTo(quotationBack);

        insuredPolicy.quotation(null);
        assertThat(insuredPolicy.getQuotation()).isNull();
    }
}
