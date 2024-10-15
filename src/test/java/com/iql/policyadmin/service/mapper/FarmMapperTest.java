package com.iql.policyadmin.service.mapper;

import static com.iql.policyadmin.domain.FarmAsserts.*;
import static com.iql.policyadmin.domain.FarmTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FarmMapperTest {

    private FarmMapper farmMapper;

    @BeforeEach
    void setUp() {
        farmMapper = new FarmMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFarmSample1();
        var actual = farmMapper.toEntity(farmMapper.toDto(expected));
        assertFarmAllPropertiesEquals(expected, actual);
    }
}
