package com.iql.policyadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuredPolicyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuredPolicyDTO.class);
        InsuredPolicyDTO insuredPolicyDTO1 = new InsuredPolicyDTO();
        insuredPolicyDTO1.setId(1L);
        InsuredPolicyDTO insuredPolicyDTO2 = new InsuredPolicyDTO();
        assertThat(insuredPolicyDTO1).isNotEqualTo(insuredPolicyDTO2);
        insuredPolicyDTO2.setId(insuredPolicyDTO1.getId());
        assertThat(insuredPolicyDTO1).isEqualTo(insuredPolicyDTO2);
        insuredPolicyDTO2.setId(2L);
        assertThat(insuredPolicyDTO1).isNotEqualTo(insuredPolicyDTO2);
        insuredPolicyDTO1.setId(null);
        assertThat(insuredPolicyDTO1).isNotEqualTo(insuredPolicyDTO2);
    }
}
