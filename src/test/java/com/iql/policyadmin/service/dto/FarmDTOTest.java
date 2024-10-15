package com.iql.policyadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FarmDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FarmDTO.class);
        FarmDTO farmDTO1 = new FarmDTO();
        farmDTO1.setId(1L);
        FarmDTO farmDTO2 = new FarmDTO();
        assertThat(farmDTO1).isNotEqualTo(farmDTO2);
        farmDTO2.setId(farmDTO1.getId());
        assertThat(farmDTO1).isEqualTo(farmDTO2);
        farmDTO2.setId(2L);
        assertThat(farmDTO1).isNotEqualTo(farmDTO2);
        farmDTO1.setId(null);
        assertThat(farmDTO1).isNotEqualTo(farmDTO2);
    }
}
