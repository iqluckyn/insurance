package com.iql.policyadmin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PolicyClaimTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PolicyClaim getPolicyClaimSample1() {
        return new PolicyClaim().id(1L).claimNumber("claimNumber1");
    }

    public static PolicyClaim getPolicyClaimSample2() {
        return new PolicyClaim().id(2L).claimNumber("claimNumber2");
    }

    public static PolicyClaim getPolicyClaimRandomSampleGenerator() {
        return new PolicyClaim().id(longCount.incrementAndGet()).claimNumber(UUID.randomUUID().toString());
    }
}
