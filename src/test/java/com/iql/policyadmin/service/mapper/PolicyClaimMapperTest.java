package com.iql.policyadmin.service.mapper;

import static com.iql.policyadmin.domain.PolicyClaimAsserts.*;
import static com.iql.policyadmin.domain.PolicyClaimTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PolicyClaimMapperTest {

    private PolicyClaimMapper policyClaimMapper;

    @BeforeEach
    void setUp() {
        policyClaimMapper = new PolicyClaimMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPolicyClaimSample1();
        var actual = policyClaimMapper.toEntity(policyClaimMapper.toDto(expected));
        assertPolicyClaimAllPropertiesEquals(expected, actual);
    }
}
