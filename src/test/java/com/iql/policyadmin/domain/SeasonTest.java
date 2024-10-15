package com.iql.policyadmin.domain;

import static com.iql.policyadmin.domain.CropTypeTestSamples.*;
import static com.iql.policyadmin.domain.SeasonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeasonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Season.class);
        Season season1 = getSeasonSample1();
        Season season2 = new Season();
        assertThat(season1).isNotEqualTo(season2);

        season2.setId(season1.getId());
        assertThat(season1).isEqualTo(season2);

        season2 = getSeasonSample2();
        assertThat(season1).isNotEqualTo(season2);
    }

    @Test
    void cropTypeTest() {
        Season season = getSeasonRandomSampleGenerator();
        CropType cropTypeBack = getCropTypeRandomSampleGenerator();

        season.setCropType(cropTypeBack);
        assertThat(season.getCropType()).isEqualTo(cropTypeBack);

        season.cropType(null);
        assertThat(season.getCropType()).isNull();
    }
}
