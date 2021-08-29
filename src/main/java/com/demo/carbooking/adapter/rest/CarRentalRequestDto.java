package com.demo.carbooking.adapter.rest;

import com.demo.carbooking.domain.CarType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CarRentalRequestDto {
    @NonNull
    private String customerId;
    @NonNull
    private CarType carType;
    @NonNull
    private LocalDate startDate;
    @NonNull
    private LocalDate endDate;
}
