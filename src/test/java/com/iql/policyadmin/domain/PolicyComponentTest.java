package com.iql.policyadmin.domain;

import static com.iql.policyadmin.domain.InsuredPolicyTestSamples.*;
import static com.iql.policyadmin.domain.PComponentTestSamples.*;
import static com.iql.policyadmin.domain.PolicyComponentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PolicyComponentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PolicyComponent.class);
        PolicyComponent policyComponent1 = getPolicyComponentSample1();
        PolicyComponent policyComponent2 = new PolicyComponent();
        assertThat(policyComponent1).isNotEqualTo(policyComponent2);

        policyComponent2.setId(policyComponent1.getId());
        assertThat(policyComponent1).isEqualTo(policyComponent2);

        policyComponent2 = getPolicyComponentSample2();
        assertThat(policyComponent1).isNotEqualTo(policyComponent2);
    }

    @Test
    void componentTest() {
        PolicyComponent policyComponent = getPolicyComponentRandomSampleGenerator();
        PComponent pComponentBack = getPComponentRandomSampleGenerator();

        policyComponent.setComponent(pComponentBack);
        assertThat(policyComponent.getComponent()).isEqualTo(pComponentBack);

        policyComponent.component(null);
        assertThat(policyComponent.getComponent()).isNull();
    }

    @Test
    void policiesTest() {
        PolicyComponent policyComponent = getPolicyComponentRandomSampleGenerator();
        InsuredPolicy insuredPolicyBack = getInsuredPolicyRandomSampleGenerator();

        policyComponent.addPolicies(insuredPolicyBack);
        assertThat(policyComponent.getPolicies()).containsOnly(insuredPolicyBack);
        assertThat(insuredPolicyBack.getComponents()).containsOnly(policyComponent);

        policyComponent.removePolicies(insuredPolicyBack);
        assertThat(policyComponent.getPolicies()).doesNotContain(insuredPolicyBack);
        assertThat(insuredPolicyBack.getComponents()).doesNotContain(policyComponent);

        policyComponent.policies(new HashSet<>(Set.of(insuredPolicyBack)));
        assertThat(policyComponent.getPolicies()).containsOnly(insuredPolicyBack);
        assertThat(insuredPolicyBack.getComponents()).containsOnly(policyComponent);

        policyComponent.setPolicies(new HashSet<>());
        assertThat(policyComponent.getPolicies()).doesNotContain(insuredPolicyBack);
        assertThat(insuredPolicyBack.getComponents()).doesNotContain(policyComponent);
    }
}
