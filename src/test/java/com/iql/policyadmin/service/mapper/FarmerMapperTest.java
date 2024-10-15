package com.iql.policyadmin.service.mapper;

import static com.iql.policyadmin.domain.FarmerAsserts.*;
import static com.iql.policyadmin.domain.FarmerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FarmerMapperTest {

    private FarmerMapper farmerMapper;

    @BeforeEach
    void setUp() {
        farmerMapper = new FarmerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFarmerSample1();
        var actual = farmerMapper.toEntity(farmerMapper.toDto(expected));
        assertFarmerAllPropertiesEquals(expected, actual);
    }
}
