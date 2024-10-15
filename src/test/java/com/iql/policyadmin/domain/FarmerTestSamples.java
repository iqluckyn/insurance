package com.iql.policyadmin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FarmerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Farmer getFarmerSample1() {
        return new Farmer()
            .id(1L)
            .firstname("firstname1")
            .lastname("lastname1")
            .email("email1")
            .position("position1")
            .phone("phone1")
            .address("address1")
            .city("city1")
            .province("province1")
            .country("country1")
            .postalCode("postalCode1");
    }

    public static Farmer getFarmerSample2() {
        return new Farmer()
            .id(2L)
            .firstname("firstname2")
            .lastname("lastname2")
            .email("email2")
            .position("position2")
            .phone("phone2")
            .address("address2")
            .city("city2")
            .province("province2")
            .country("country2")
            .postalCode("postalCode2");
    }

    public static Farmer getFarmerRandomSampleGenerator() {
        return new Farmer()
            .id(longCount.incrementAndGet())
            .firstname(UUID.randomUUID().toString())
            .lastname(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .position(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .province(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .postalCode(UUID.randomUUID().toString());
    }
}
