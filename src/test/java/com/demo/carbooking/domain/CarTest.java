package com.demo.carbooking.domain;

import com.demo.carbooking.common.util.DateTime;
import com.demo.carbooking.common.util.DateTimeProvider;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CarTest {
    @Test
    public void should_return_true_when_car_not_rented_before() {
        Car car = new Car(null, Collections.emptyList());
        assertThat(car.isCarAvailableBetween(LocalDate.of(2021, 8, 28), LocalDate.of(2021, 8,29))).isTrue();
    }


    @Test
    public void should_return_false_when_car_with_order_should_end_but_not_end() {
        String bookingId = UUID.randomUUID().toString();
        mockDate(LocalDate.of(2021, 8, 29));
        BookingOrder order = new BookingOrder(bookingId, "customerId", OrderState.BOOKED,
                LocalDate.of(2021, 8, 27), LocalDate.of(2021, 8, 28), null);
        CarBooking carBooking = new CarBooking(bookingId, order, Collections.emptyList());

        Car car = new Car(null, Collections.singletonList(carBooking));
        assertThat(car.isCarAvailableBetween(LocalDate.of(2021, 8, 28), LocalDate.of(2021, 8,29))).isFalse();
    }

    @Test
    public void should_return_true_when_car_not_rented_between_those_dates() {
        String bookingId = UUID.randomUUID().toString();
        mockDate(LocalDate.of(2021, 8, 27));
        BookingOrder order = new BookingOrder(bookingId, "customerId", OrderState.BOOKED,
                LocalDate.of(2021, 8, 28), LocalDate.of(2021, 8, 28), null);
        CarRentalInfo carRentalInfo = new CarRentalInfo("car1", LocalDate.of(2021, 8, 28), bookingId);
        CarBooking carBooking = new CarBooking(bookingId, order, Collections.singletonList(carRentalInfo));
        Car car = new Car(null, Collections.singletonList(carBooking));
        assertThat(car.isCarAvailableBetween(LocalDate.of(2021, 8, 29), LocalDate.of(2021, 8,30))).isTrue();
    }

    @Test
    public void should_return_false_when_car_rented_between_those_dates() {
        String bookingId = UUID.randomUUID().toString();
        mockDate(LocalDate.of(2021, 8, 29));
        BookingOrder order = new BookingOrder(bookingId, "customerId", OrderState.BOOKED,
                LocalDate.of(2021, 8, 30), LocalDate.of(2021, 8, 30), null);
        CarRentalInfo carRentalInfo = new CarRentalInfo("car1", LocalDate.of(2021, 8, 30), bookingId);
        CarBooking carBooking = new CarBooking(bookingId, order, Collections.singletonList(carRentalInfo));
        Car car = new Car(null, Collections.singletonList(carBooking));
        assertThat(car.isCarAvailableBetween(LocalDate.of(2021, 8, 29), LocalDate.of(2021, 8,31))).isFalse();
    }

    protected void mockDate(LocalDate localDate) {
        DateTimeProvider dateTimeProvider = mock(DateTimeProvider.class);
        when(dateTimeProvider.nowDate()).thenReturn(localDate);
        DateTime.set(dateTimeProvider);
    }
}