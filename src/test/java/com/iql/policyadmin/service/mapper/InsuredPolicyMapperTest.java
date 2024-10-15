package com.iql.policyadmin.service.mapper;

import static com.iql.policyadmin.domain.InsuredPolicyAsserts.*;
import static com.iql.policyadmin.domain.InsuredPolicyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InsuredPolicyMapperTest {

    private InsuredPolicyMapper insuredPolicyMapper;

    @BeforeEach
    void setUp() {
        insuredPolicyMapper = new InsuredPolicyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInsuredPolicySample1();
        var actual = insuredPolicyMapper.toEntity(insuredPolicyMapper.toDto(expected));
        assertInsuredPolicyAllPropertiesEquals(expected, actual);
    }
}
