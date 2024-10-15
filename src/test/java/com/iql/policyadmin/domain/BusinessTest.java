package com.iql.policyadmin.domain;

import static com.iql.policyadmin.domain.BusinessTestSamples.*;
import static com.iql.policyadmin.domain.BusinessTypeTestSamples.*;
import static com.iql.policyadmin.domain.FarmerTestSamples.*;
import static com.iql.policyadmin.domain.QuotationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BusinessTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Business.class);
        Business business1 = getBusinessSample1();
        Business business2 = new Business();
        assertThat(business1).isNotEqualTo(business2);

        business2.setId(business1.getId());
        assertThat(business1).isEqualTo(business2);

        business2 = getBusinessSample2();
        assertThat(business1).isNotEqualTo(business2);
    }

    @Test
    void farmerTest() {
        Business business = getBusinessRandomSampleGenerator();
        Farmer farmerBack = getFarmerRandomSampleGenerator();

        business.addFarmer(farmerBack);
        assertThat(business.getFarmers()).containsOnly(farmerBack);
        assertThat(farmerBack.getBusiness()).isEqualTo(business);

        business.removeFarmer(farmerBack);
        assertThat(business.getFarmers()).doesNotContain(farmerBack);
        assertThat(farmerBack.getBusiness()).isNull();

        business.farmers(new HashSet<>(Set.of(farmerBack)));
        assertThat(business.getFarmers()).containsOnly(farmerBack);
        assertThat(farmerBack.getBusiness()).isEqualTo(business);

        business.setFarmers(new HashSet<>());
        assertThat(business.getFarmers()).doesNotContain(farmerBack);
        assertThat(farmerBack.getBusiness()).isNull();
    }

    @Test
    void quotationTest() {
        Business business = getBusinessRandomSampleGenerator();
        Quotation quotationBack = getQuotationRandomSampleGenerator();

        business.addQuotation(quotationBack);
        assertThat(business.getQuotations()).containsOnly(quotationBack);
        assertThat(quotationBack.getBusiness()).isEqualTo(business);

        business.removeQuotation(quotationBack);
        assertThat(business.getQuotations()).doesNotContain(quotationBack);
        assertThat(quotationBack.getBusiness()).isNull();

        business.quotations(new HashSet<>(Set.of(quotationBack)));
        assertThat(business.getQuotations()).containsOnly(quotationBack);
        assertThat(quotationBack.getBusiness()).isEqualTo(business);

        business.setQuotations(new HashSet<>());
        assertThat(business.getQuotations()).doesNotContain(quotationBack);
        assertThat(quotationBack.getBusiness()).isNull();
    }

    @Test
    void businessTypeTest() {
        Business business = getBusinessRandomSampleGenerator();
        BusinessType businessTypeBack = getBusinessTypeRandomSampleGenerator();

        business.setBusinessType(businessTypeBack);
        assertThat(business.getBusinessType()).isEqualTo(businessTypeBack);

        business.businessType(null);
        assertThat(business.getBusinessType()).isNull();
    }
}
