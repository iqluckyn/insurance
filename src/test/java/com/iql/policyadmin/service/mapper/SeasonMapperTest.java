package com.iql.policyadmin.service.mapper;

import static com.iql.policyadmin.domain.SeasonAsserts.*;
import static com.iql.policyadmin.domain.SeasonTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SeasonMapperTest {

    private SeasonMapper seasonMapper;

    @BeforeEach
    void setUp() {
        seasonMapper = new SeasonMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSeasonSample1();
        var actual = seasonMapper.toEntity(seasonMapper.toDto(expected));
        assertSeasonAllPropertiesEquals(expected, actual);
    }
}
