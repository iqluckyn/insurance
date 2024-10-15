package com.iql.policyadmin.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuotationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Quotation getQuotationSample1() {
        return new Quotation().id(1L).lengthOfRiskPeriod(1).depth(1).claimsFrequency(1);
    }

    public static Quotation getQuotationSample2() {
        return new Quotation().id(2L).lengthOfRiskPeriod(2).depth(2).claimsFrequency(2);
    }

    public static Quotation getQuotationRandomSampleGenerator() {
        return new Quotation()
            .id(longCount.incrementAndGet())
            .lengthOfRiskPeriod(intCount.incrementAndGet())
            .depth(intCount.incrementAndGet())
            .claimsFrequency(intCount.incrementAndGet());
    }
}
