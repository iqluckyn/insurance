package com.iql.policyadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CropTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CropTypeDTO.class);
        CropTypeDTO cropTypeDTO1 = new CropTypeDTO();
        cropTypeDTO1.setId(1L);
        CropTypeDTO cropTypeDTO2 = new CropTypeDTO();
        assertThat(cropTypeDTO1).isNotEqualTo(cropTypeDTO2);
        cropTypeDTO2.setId(cropTypeDTO1.getId());
        assertThat(cropTypeDTO1).isEqualTo(cropTypeDTO2);
        cropTypeDTO2.setId(2L);
        assertThat(cropTypeDTO1).isNotEqualTo(cropTypeDTO2);
        cropTypeDTO1.setId(null);
        assertThat(cropTypeDTO1).isNotEqualTo(cropTypeDTO2);
    }
}
