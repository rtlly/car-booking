package com.demo.carbooking.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

public final class LocalDateUtil {
    private static final Logger logger = LoggerFactory.getLogger(LocalDateUtil.class);

    private LocalDateUtil() {
    }

    public static boolean isBetween(LocalDate startDate, LocalDate endDate, LocalDate date) {
        if (startDate == null || endDate == null || date == null) {
            return false;
        }

        return date.isEqual(startDate) || date.isEqual(endDate)
                || (date.isAfter(startDate) && date.isBefore(endDate));
    }

    public static boolean isBefore(LocalDate currentDate, LocalDate standardDate) {
        if (currentDate == null || standardDate == null) {
            return false;
        }

        return currentDate.isBefore(standardDate);
    }

    public static int betweenDayAbs(LocalDate start, LocalDate end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return 0;
        }

        return Math.abs((int) start.until(end, DAYS));
    }

    public static boolean isBeforeOrEqual(LocalDate currentDate, LocalDate standardDate) {
        if (currentDate == null || standardDate == null) {
            return false;
        }

        return currentDate.isEqual(standardDate) || currentDate.isBefore(standardDate);
    }

    public static boolean isAfterOrEqual(LocalDate currentDate, LocalDate standardDate) {
        if (currentDate == null || standardDate == null) {
            return false;
        }

        return currentDate.isEqual(standardDate) || currentDate.isAfter(standardDate);
    }

}
