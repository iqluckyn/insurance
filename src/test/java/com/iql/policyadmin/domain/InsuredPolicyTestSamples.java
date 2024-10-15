package com.iql.policyadmin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InsuredPolicyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InsuredPolicy getInsuredPolicySample1() {
        return new InsuredPolicy().id(1L).policyNumber("policyNumber1");
    }

    public static InsuredPolicy getInsuredPolicySample2() {
        return new InsuredPolicy().id(2L).policyNumber("policyNumber2");
    }

    public static InsuredPolicy getInsuredPolicyRandomSampleGenerator() {
        return new InsuredPolicy().id(longCount.incrementAndGet()).policyNumber(UUID.randomUUID().toString());
    }
}
