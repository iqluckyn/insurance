package com.iql.policyadmin.domain;

import static com.iql.policyadmin.domain.PComponentTestSamples.*;
import static com.iql.policyadmin.domain.PolicyComponentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PComponentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PComponent.class);
        PComponent pComponent1 = getPComponentSample1();
        PComponent pComponent2 = new PComponent();
        assertThat(pComponent1).isNotEqualTo(pComponent2);

        pComponent2.setId(pComponent1.getId());
        assertThat(pComponent1).isEqualTo(pComponent2);

        pComponent2 = getPComponentSample2();
        assertThat(pComponent1).isNotEqualTo(pComponent2);
    }

    @Test
    void policyComponentTest() {
        PComponent pComponent = getPComponentRandomSampleGenerator();
        PolicyComponent policyComponentBack = getPolicyComponentRandomSampleGenerator();

        pComponent.addPolicyComponent(policyComponentBack);
        assertThat(pComponent.getPolicyComponents()).containsOnly(policyComponentBack);
        assertThat(policyComponentBack.getComponent()).isEqualTo(pComponent);

        pComponent.removePolicyComponent(policyComponentBack);
        assertThat(pComponent.getPolicyComponents()).doesNotContain(policyComponentBack);
        assertThat(policyComponentBack.getComponent()).isNull();

        pComponent.policyComponents(new HashSet<>(Set.of(policyComponentBack)));
        assertThat(pComponent.getPolicyComponents()).containsOnly(policyComponentBack);
        assertThat(policyComponentBack.getComponent()).isEqualTo(pComponent);

        pComponent.setPolicyComponents(new HashSet<>());
        assertThat(pComponent.getPolicyComponents()).doesNotContain(policyComponentBack);
        assertThat(policyComponentBack.getComponent()).isNull();
    }
}
