package com.iql.policyadmin.service.mapper;

import static com.iql.policyadmin.domain.PolicyComponentAsserts.*;
import static com.iql.policyadmin.domain.PolicyComponentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PolicyComponentMapperTest {

    private PolicyComponentMapper policyComponentMapper;

    @BeforeEach
    void setUp() {
        policyComponentMapper = new PolicyComponentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPolicyComponentSample1();
        var actual = policyComponentMapper.toEntity(policyComponentMapper.toDto(expected));
        assertPolicyComponentAllPropertiesEquals(expected, actual);
    }
}
