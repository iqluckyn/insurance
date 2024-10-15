package com.iql.policyadmin.service.mapper;

import static com.iql.policyadmin.domain.PComponentAsserts.*;
import static com.iql.policyadmin.domain.PComponentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PComponentMapperTest {

    private PComponentMapper pComponentMapper;

    @BeforeEach
    void setUp() {
        pComponentMapper = new PComponentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPComponentSample1();
        var actual = pComponentMapper.toEntity(pComponentMapper.toDto(expected));
        assertPComponentAllPropertiesEquals(expected, actual);
    }
}
