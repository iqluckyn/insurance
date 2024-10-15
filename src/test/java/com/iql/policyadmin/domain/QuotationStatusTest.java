package com.iql.policyadmin.domain;

import static com.iql.policyadmin.domain.QuotationStatusTestSamples.*;
import static com.iql.policyadmin.domain.QuotationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class QuotationStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotationStatus.class);
        QuotationStatus quotationStatus1 = getQuotationStatusSample1();
        QuotationStatus quotationStatus2 = new QuotationStatus();
        assertThat(quotationStatus1).isNotEqualTo(quotationStatus2);

        quotationStatus2.setId(quotationStatus1.getId());
        assertThat(quotationStatus1).isEqualTo(quotationStatus2);

        quotationStatus2 = getQuotationStatusSample2();
        assertThat(quotationStatus1).isNotEqualTo(quotationStatus2);
    }

    @Test
    void quotationsTest() {
        QuotationStatus quotationStatus = getQuotationStatusRandomSampleGenerator();
        Quotation quotationBack = getQuotationRandomSampleGenerator();

        quotationStatus.addQuotations(quotationBack);
        assertThat(quotationStatus.getQuotations()).containsOnly(quotationBack);
        assertThat(quotationBack.getQuotationStatus()).isEqualTo(quotationStatus);

        quotationStatus.removeQuotations(quotationBack);
        assertThat(quotationStatus.getQuotations()).doesNotContain(quotationBack);
        assertThat(quotationBack.getQuotationStatus()).isNull();

        quotationStatus.quotations(new HashSet<>(Set.of(quotationBack)));
        assertThat(quotationStatus.getQuotations()).containsOnly(quotationBack);
        assertThat(quotationBack.getQuotationStatus()).isEqualTo(quotationStatus);

        quotationStatus.setQuotations(new HashSet<>());
        assertThat(quotationStatus.getQuotations()).doesNotContain(quotationBack);
        assertThat(quotationBack.getQuotationStatus()).isNull();
    }
}
