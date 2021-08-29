package com.demo.carbooking.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DateTimeProvider {
    LocalDate nowDate();

    LocalDateTime now();
}
