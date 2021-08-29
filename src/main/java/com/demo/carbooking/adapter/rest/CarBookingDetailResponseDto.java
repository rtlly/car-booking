package com.demo.carbooking.adapter.rest;

import com.demo.carbooking.domain.CarBooking;
import com.demo.carbooking.domain.CarModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CarBookingDetailResponseDto {
    private CarModel carModel;
    private CarBooking carBooking;
}
