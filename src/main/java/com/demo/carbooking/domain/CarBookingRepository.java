package com.demo.carbooking.domain;


import com.demo.carbooking.domain.concept.Repository;

import java.util.List;
import java.util.Optional;

public interface CarBookingRepository extends Repository {

    void save(CarBooking carBooking);

    List<CarBooking> findNotEndedBy(String carId);
    Optional<CarBooking> findByOrderId(String orderId);

    void updateBooking(CarBooking booking);
}
