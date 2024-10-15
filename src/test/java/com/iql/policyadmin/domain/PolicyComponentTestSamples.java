package com.iql.policyadmin.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PolicyComponentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PolicyComponent getPolicyComponentSample1() {
        return new PolicyComponent().id(1L);
    }

    public static PolicyComponent getPolicyComponentSample2() {
        return new PolicyComponent().id(2L);
    }

    public static PolicyComponent getPolicyComponentRandomSampleGenerator() {
        return new PolicyComponent().id(longCount.incrementAndGet());
    }
}
