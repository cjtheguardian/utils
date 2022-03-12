package com.cj.theguardian.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

public class Test {

    @org.junit.Test
    public void testformat() {
        System.out.println(String.format("status: synced %s/%s files (%s%%) , and %s/%s bytes (%s%%)",
                1, 3, BigDecimal.valueOf(0.33),
                1, 5, BigDecimal.valueOf(0.2)));
    }

}

