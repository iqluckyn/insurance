package com.iql.policyadmin.domain;

import static com.iql.policyadmin.domain.CropTypeTestSamples.*;
import static com.iql.policyadmin.domain.SeasonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iql.policyadmin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CropTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CropType.class);
        CropType cropType1 = getCropTypeSample1();
        CropType cropType2 = new CropType();
        assertThat(cropType1).isNotEqualTo(cropType2);

        cropType2.setId(cropType1.getId());
        assertThat(cropType1).isEqualTo(cropType2);

        cropType2 = getCropTypeSample2();
        assertThat(cropType1).isNotEqualTo(cropType2);
    }

    @Test
    void seasonTest() {
        CropType cropType = getCropTypeRandomSampleGenerator();
        Season seasonBack = getSeasonRandomSampleGenerator();

        cropType.addSeason(seasonBack);
        assertThat(cropType.getSeasons()).containsOnly(seasonBack);
        assertThat(seasonBack.getCropType()).isEqualTo(cropType);

        cropType.removeSeason(seasonBack);
        assertThat(cropType.getSeasons()).doesNotContain(seasonBack);
        assertThat(seasonBack.getCropType()).isNull();

        cropType.seasons(new HashSet<>(Set.of(seasonBack)));
        assertThat(cropType.getSeasons()).containsOnly(seasonBack);
        assertThat(seasonBack.getCropType()).isEqualTo(cropType);

        cropType.setSeasons(new HashSet<>());
        assertThat(cropType.getSeasons()).doesNotContain(seasonBack);
        assertThat(seasonBack.getCropType()).isNull();
    }
}
