package com.iql.policyadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PComponentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PComponentDTO.class);
        PComponentDTO pComponentDTO1 = new PComponentDTO();
        pComponentDTO1.setId(1L);
        PComponentDTO pComponentDTO2 = new PComponentDTO();
        assertThat(pComponentDTO1).isNotEqualTo(pComponentDTO2);
        pComponentDTO2.setId(pComponentDTO1.getId());
        assertThat(pComponentDTO1).isEqualTo(pComponentDTO2);
        pComponentDTO2.setId(2L);
        assertThat(pComponentDTO1).isNotEqualTo(pComponentDTO2);
        pComponentDTO1.setId(null);
        assertThat(pComponentDTO1).isNotEqualTo(pComponentDTO2);
    }
}
