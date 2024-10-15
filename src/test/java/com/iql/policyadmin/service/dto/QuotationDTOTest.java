package com.iql.policyadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuotationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotationDTO.class);
        QuotationDTO quotationDTO1 = new QuotationDTO();
        quotationDTO1.setId(1L);
        QuotationDTO quotationDTO2 = new QuotationDTO();
        assertThat(quotationDTO1).isNotEqualTo(quotationDTO2);
        quotationDTO2.setId(quotationDTO1.getId());
        assertThat(quotationDTO1).isEqualTo(quotationDTO2);
        quotationDTO2.setId(2L);
        assertThat(quotationDTO1).isNotEqualTo(quotationDTO2);
        quotationDTO1.setId(null);
        assertThat(quotationDTO1).isNotEqualTo(quotationDTO2);
    }
}
