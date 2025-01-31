package com.iql.policyadmin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BusinessTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BusinessType getBusinessTypeSample1() {
        return new BusinessType().id(1L).name("name1").description("description1");
    }

    public static BusinessType getBusinessTypeSample2() {
        return new BusinessType().id(2L).name("name2").description("description2");
    }

    public static BusinessType getBusinessTypeRandomSampleGenerator() {
        return new BusinessType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
