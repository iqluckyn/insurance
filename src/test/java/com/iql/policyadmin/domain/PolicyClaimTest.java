package com.iql.policyadmin.domain;

import static com.iql.policyadmin.domain.InsuredPolicyTestSamples.*;
import static com.iql.policyadmin.domain.PolicyClaimTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PolicyClaimTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PolicyClaim.class);
        PolicyClaim policyClaim1 = getPolicyClaimSample1();
        PolicyClaim policyClaim2 = new PolicyClaim();
        assertThat(policyClaim1).isNotEqualTo(policyClaim2);

        policyClaim2.setId(policyClaim1.getId());
        assertThat(policyClaim1).isEqualTo(policyClaim2);

        policyClaim2 = getPolicyClaimSample2();
        assertThat(policyClaim1).isNotEqualTo(policyClaim2);
    }

    @Test
    void policyTest() {
        PolicyClaim policyClaim = getPolicyClaimRandomSampleGenerator();
        InsuredPolicy insuredPolicyBack = getInsuredPolicyRandomSampleGenerator();

        policyClaim.setPolicy(insuredPolicyBack);
        assertThat(policyClaim.getPolicy()).isEqualTo(insuredPolicyBack);

        policyClaim.policy(null);
        assertThat(policyClaim.getPolicy()).isNull();
    }
}
