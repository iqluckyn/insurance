package com.iql.policyadmin.service.mapper;

import static com.iql.policyadmin.domain.BusinessAsserts.*;
import static com.iql.policyadmin.domain.BusinessTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusinessMapperTest {

    private BusinessMapper businessMapper;

    @BeforeEach
    void setUp() {
        businessMapper = new BusinessMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBusinessSample1();
        var actual = businessMapper.toEntity(businessMapper.toDto(expected));
        assertBusinessAllPropertiesEquals(expected, actual);
    }
}
