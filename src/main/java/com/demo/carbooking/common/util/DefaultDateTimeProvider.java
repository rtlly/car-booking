package com.demo.carbooking.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DefaultDateTimeProvider implements DateTimeProvider {
    @Override
    public LocalDate nowDate() {
        return LocalDate.now();
    }

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
