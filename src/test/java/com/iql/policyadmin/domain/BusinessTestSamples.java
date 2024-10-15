package com.iql.policyadmin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BusinessTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Business getBusinessSample1() {
        return new Business().id(1L).registeredName("registeredName1").organisationName("organisationName1").vatNumber("vatNumber1");
    }

    public static Business getBusinessSample2() {
        return new Business().id(2L).registeredName("registeredName2").organisationName("organisationName2").vatNumber("vatNumber2");
    }

    public static Business getBusinessRandomSampleGenerator() {
        return new Business()
            .id(longCount.incrementAndGet())
            .registeredName(UUID.randomUUID().toString())
            .organisationName(UUID.randomUUID().toString())
            .vatNumber(UUID.randomUUID().toString());
    }
}
