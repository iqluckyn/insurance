package com.iql.policyadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PolicyComponentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PolicyComponentDTO.class);
        PolicyComponentDTO policyComponentDTO1 = new PolicyComponentDTO();
        policyComponentDTO1.setId(1L);
        PolicyComponentDTO policyComponentDTO2 = new PolicyComponentDTO();
        assertThat(policyComponentDTO1).isNotEqualTo(policyComponentDTO2);
        policyComponentDTO2.setId(policyComponentDTO1.getId());
        assertThat(policyComponentDTO1).isEqualTo(policyComponentDTO2);
        policyComponentDTO2.setId(2L);
        assertThat(policyComponentDTO1).isNotEqualTo(policyComponentDTO2);
        policyComponentDTO1.setId(null);
        assertThat(policyComponentDTO1).isNotEqualTo(policyComponentDTO2);
    }
}
