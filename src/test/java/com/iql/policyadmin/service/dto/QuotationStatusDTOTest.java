package com.iql.policyadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuotationStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotationStatusDTO.class);
        QuotationStatusDTO quotationStatusDTO1 = new QuotationStatusDTO();
        quotationStatusDTO1.setId(1L);
        QuotationStatusDTO quotationStatusDTO2 = new QuotationStatusDTO();
        assertThat(quotationStatusDTO1).isNotEqualTo(quotationStatusDTO2);
        quotationStatusDTO2.setId(quotationStatusDTO1.getId());
        assertThat(quotationStatusDTO1).isEqualTo(quotationStatusDTO2);
        quotationStatusDTO2.setId(2L);
        assertThat(quotationStatusDTO1).isNotEqualTo(quotationStatusDTO2);
        quotationStatusDTO1.setId(null);
        assertThat(quotationStatusDTO1).isNotEqualTo(quotationStatusDTO2);
    }
}
