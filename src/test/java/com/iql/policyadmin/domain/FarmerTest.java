package com.iql.policyadmin.domain;

import static com.iql.policyadmin.domain.BusinessTestSamples.*;
import static com.iql.policyadmin.domain.FarmTestSamples.*;
import static com.iql.policyadmin.domain.FarmerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FarmerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Farmer.class);
        Farmer farmer1 = getFarmerSample1();
        Farmer farmer2 = new Farmer();
        assertThat(farmer1).isNotEqualTo(farmer2);

        farmer2.setId(farmer1.getId());
        assertThat(farmer1).isEqualTo(farmer2);

        farmer2 = getFarmerSample2();
        assertThat(farmer1).isNotEqualTo(farmer2);
    }

    @Test
    void farmTest() {
        Farmer farmer = getFarmerRandomSampleGenerator();
        Farm farmBack = getFarmRandomSampleGenerator();

        farmer.addFarm(farmBack);
        assertThat(farmer.getFarms()).containsOnly(farmBack);
        assertThat(farmBack.getFarmer()).isEqualTo(farmer);

        farmer.removeFarm(farmBack);
        assertThat(farmer.getFarms()).doesNotContain(farmBack);
        assertThat(farmBack.getFarmer()).isNull();

        farmer.farms(new HashSet<>(Set.of(farmBack)));
        assertThat(farmer.getFarms()).containsOnly(farmBack);
        assertThat(farmBack.getFarmer()).isEqualTo(farmer);

        farmer.setFarms(new HashSet<>());
        assertThat(farmer.getFarms()).doesNotContain(farmBack);
        assertThat(farmBack.getFarmer()).isNull();
    }

    @Test
    void businessTest() {
        Farmer farmer = getFarmerRandomSampleGenerator();
        Business businessBack = getBusinessRandomSampleGenerator();

        farmer.setBusiness(businessBack);
        assertThat(farmer.getBusiness()).isEqualTo(businessBack);

        farmer.business(null);
        assertThat(farmer.getBusiness()).isNull();
    }
}
