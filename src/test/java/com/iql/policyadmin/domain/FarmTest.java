package com.iql.policyadmin.domain;

import static com.iql.policyadmin.domain.CropTypeTestSamples.*;
import static com.iql.policyadmin.domain.FarmTestSamples.*;
import static com.iql.policyadmin.domain.FarmerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FarmTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Farm.class);
        Farm farm1 = getFarmSample1();
        Farm farm2 = new Farm();
        assertThat(farm1).isNotEqualTo(farm2);

        farm2.setId(farm1.getId());
        assertThat(farm1).isEqualTo(farm2);

        farm2 = getFarmSample2();
        assertThat(farm1).isNotEqualTo(farm2);
    }

    @Test
    void cropTypeTest() {
        Farm farm = getFarmRandomSampleGenerator();
        CropType cropTypeBack = getCropTypeRandomSampleGenerator();

        farm.setCropType(cropTypeBack);
        assertThat(farm.getCropType()).isEqualTo(cropTypeBack);

        farm.cropType(null);
        assertThat(farm.getCropType()).isNull();
    }

    @Test
    void farmerTest() {
        Farm farm = getFarmRandomSampleGenerator();
        Farmer farmerBack = getFarmerRandomSampleGenerator();

        farm.setFarmer(farmerBack);
        assertThat(farm.getFarmer()).isEqualTo(farmerBack);

        farm.farmer(null);
        assertThat(farm.getFarmer()).isNull();
    }
}
