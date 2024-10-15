package com.iql.policyadmin.service.mapper;

import static com.iql.policyadmin.domain.CropTypeAsserts.*;
import static com.iql.policyadmin.domain.CropTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CropTypeMapperTest {

    private CropTypeMapper cropTypeMapper;

    @BeforeEach
    void setUp() {
        cropTypeMapper = new CropTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCropTypeSample1();
        var actual = cropTypeMapper.toEntity(cropTypeMapper.toDto(expected));
        assertCropTypeAllPropertiesEquals(expected, actual);
    }
}
