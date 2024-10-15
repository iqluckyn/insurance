package com.iql.policyadmin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QuotationStatusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static QuotationStatus getQuotationStatusSample1() {
        return new QuotationStatus().id(1L).statusName("statusName1").statusCode("statusCode1").description("description1");
    }

    public static QuotationStatus getQuotationStatusSample2() {
        return new QuotationStatus().id(2L).statusName("statusName2").statusCode("statusCode2").description("description2");
    }

    public static QuotationStatus getQuotationStatusRandomSampleGenerator() {
        return new QuotationStatus()
            .id(longCount.incrementAndGet())
            .statusName(UUID.randomUUID().toString())
            .statusCode(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
