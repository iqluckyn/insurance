package com.iql.policyadmin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FarmTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Farm getFarmSample1() {
        return new Farm().id(1L).cellIdentifier("cellIdentifier1");
    }

    public static Farm getFarmSample2() {
        return new Farm().id(2L).cellIdentifier("cellIdentifier2");
    }

    public static Farm getFarmRandomSampleGenerator() {
        return new Farm().id(longCount.incrementAndGet()).cellIdentifier(UUID.randomUUID().toString());
    }
}
