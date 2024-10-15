package com.iql.policyadmin.service.mapper;

import static com.iql.policyadmin.domain.QuotationStatusAsserts.*;
import static com.iql.policyadmin.domain.QuotationStatusTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuotationStatusMapperTest {

    private QuotationStatusMapper quotationStatusMapper;

    @BeforeEach
    void setUp() {
        quotationStatusMapper = new QuotationStatusMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuotationStatusSample1();
        var actual = quotationStatusMapper.toEntity(quotationStatusMapper.toDto(expected));
        assertQuotationStatusAllPropertiesEquals(expected, actual);
    }
}
