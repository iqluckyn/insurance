package com.iql.policyadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PolicyClaimDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PolicyClaimDTO.class);
        PolicyClaimDTO policyClaimDTO1 = new PolicyClaimDTO();
        policyClaimDTO1.setId(1L);
        PolicyClaimDTO policyClaimDTO2 = new PolicyClaimDTO();
        assertThat(policyClaimDTO1).isNotEqualTo(policyClaimDTO2);
        policyClaimDTO2.setId(policyClaimDTO1.getId());
        assertThat(policyClaimDTO1).isEqualTo(policyClaimDTO2);
        policyClaimDTO2.setId(2L);
        assertThat(policyClaimDTO1).isNotEqualTo(policyClaimDTO2);
        policyClaimDTO1.setId(null);
        assertThat(policyClaimDTO1).isNotEqualTo(policyClaimDTO2);
    }
}
