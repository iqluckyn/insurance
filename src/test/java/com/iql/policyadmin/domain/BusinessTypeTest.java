package com.iql.policyadmin.domain;

import static com.iql.policyadmin.domain.BusinessTestSamples.*;
import static com.iql.policyadmin.domain.BusinessTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BusinessTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessType.class);
        BusinessType businessType1 = getBusinessTypeSample1();
        BusinessType businessType2 = new BusinessType();
        assertThat(businessType1).isNotEqualTo(businessType2);

        businessType2.setId(businessType1.getId());
        assertThat(businessType1).isEqualTo(businessType2);

        businessType2 = getBusinessTypeSample2();
        assertThat(businessType1).isNotEqualTo(businessType2);
    }

    @Test
    void businessTest() {
        BusinessType businessType = getBusinessTypeRandomSampleGenerator();
        Business businessBack = getBusinessRandomSampleGenerator();

        businessType.addBusiness(businessBack);
        assertThat(businessType.getBusinesses()).containsOnly(businessBack);
        assertThat(businessBack.getBusinessType()).isEqualTo(businessType);

        businessType.removeBusiness(businessBack);
        assertThat(businessType.getBusinesses()).doesNotContain(businessBack);
        assertThat(businessBack.getBusinessType()).isNull();

        businessType.businesses(new HashSet<>(Set.of(businessBack)));
        assertThat(businessType.getBusinesses()).containsOnly(businessBack);
        assertThat(businessBack.getBusinessType()).isEqualTo(businessType);

        businessType.setBusinesses(new HashSet<>());
        assertThat(businessType.getBusinesses()).doesNotContain(businessBack);
        assertThat(businessBack.getBusinessType()).isNull();
    }
}
