package com.demo.carbooking.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateTime {
    private static DateTimeProvider dateTimeProvider = new DefaultDateTimeProvider();

    public static void set(DateTimeProvider dateTimeProvider) {
        DateTime.dateTimeProvider = dateTimeProvider;
    }

    public static LocalDate toDate() {
        return dateTimeProvider.nowDate();
    }

    public static LocalDateTime now() {
        return dateTimeProvider.now();
    }

    public static void reset() {
        dateTimeProvider = new DefaultDateTimeProvider();
    }
}
