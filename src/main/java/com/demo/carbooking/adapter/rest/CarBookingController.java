package com.demo.carbooking.adapter.rest;

import com.demo.carbooking.application.CarBookingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/car-booking")
public class CarBookingController {
    private final CarBookingService carBookingService;


    @SneakyThrows
    @PostMapping("/rent")
    public String rentCar(@RequestBody CarRentalRequestDto carRentalRequest) {
        return carBookingService.rent(carRentalRequest);
    }

    @SneakyThrows
    @PutMapping("/retrieve/{id}")
    public void retrieveCar(@PathVariable("id") String orderId) {
        carBookingService.retrieveCar(orderId);
    }

    @SneakyThrows
    @PutMapping("/return/{id}")
    public void returnCar(@PathVariable("id") String orderId) {
        carBookingService.returnCar(orderId);
    }

    @SneakyThrows
    @PutMapping("/cancel/{id}")
    public void cancel(@PathVariable("id") String orderId) {
        carBookingService.cancelOrder(orderId);
    }

    @SneakyThrows
    @GetMapping("/detail/{id}")
    public CarBookingDetailResponseDto getOrderDetail(@PathVariable("id") String orderId) {
        return carBookingService.getBookingDetail(orderId);
    }

    @SneakyThrows
    @GetMapping("/available-cars")
    public List<CarOverviewInfo> getOrderDetail(@RequestParam("startDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
                                                @RequestParam("endDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate) {
        return carBookingService.getAllAvailableCarsBetweenTheseDays(startDate, endDate);
    }
}
