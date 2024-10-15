package com.iql.policyadmin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CropTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CropType getCropTypeSample1() {
        return new CropType().id(1L).cropName("cropName1");
    }

    public static CropType getCropTypeSample2() {
        return new CropType().id(2L).cropName("cropName2");
    }

    public static CropType getCropTypeRandomSampleGenerator() {
        return new CropType().id(longCount.incrementAndGet()).cropName(UUID.randomUUID().toString());
    }
}
