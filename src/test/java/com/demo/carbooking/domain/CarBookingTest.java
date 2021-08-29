package com.demo.carbooking.domain;

import com.demo.carbooking.common.util.DateTime;
import com.demo.carbooking.common.util.DateTimeProvider;
import com.demo.carbooking.domain.concept.TrackState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CarBookingTest {
    @Test
    public void should_return_true_when_car_is_not_return_until_due_date() {
        String bookingId = UUID.randomUUID().toString();
        mockDate(LocalDate.of(2021, 8, 29));
        BookingOrder order = new BookingOrder(bookingId, "customerId", OrderState.BOOKED,
                LocalDate.of(2021, 8, 27), LocalDate.of(2021, 8, 28), null);
        CarBooking carBooking = new CarBooking(bookingId, order, Collections.emptyList());
        assertThat(carBooking.shouldReturnButNotReturnUntilNow()).isTrue();
    }

    @Test
    public void should_return_false_when_order_end_date_is_not_before_today() {
        String bookingId = UUID.randomUUID().toString();
        mockDate(LocalDate.of(2021, 8, 29));
        BookingOrder order = new BookingOrder(bookingId, "customerId", OrderState.BOOKED,
                LocalDate.of(2021, 8, 27), LocalDate.of(2021, 8, 29), null);
        CarBooking carBooking = new CarBooking(bookingId, order, Collections.emptyList());
        assertThat(carBooking.shouldReturnButNotReturnUntilNow()).isFalse();
    }

    @Test
    public void should_return_false_when_car_has_been_returned() {
        String bookingId = UUID.randomUUID().toString();
        mockDate(LocalDate.of(2021, 8, 29));
        BookingOrder order = new BookingOrder(bookingId, "customerId", OrderState.BOOKED,
                LocalDate.of(2021, 8, 27), LocalDate.of(2021, 8, 28),  LocalDate.of(2021, 8, 29));
        CarBooking carBooking = new CarBooking(bookingId, order, Collections.emptyList());
        assertThat(carBooking.shouldReturnButNotReturnUntilNow()).isFalse();
    }

    @Test
    public void should_rent_records_after_returned_date_mark_as_deleted_when_car_returned_in_advance() {
        CarBooking carBooking = mockCarBooking();
        mockDate(LocalDate.of(2021, 8, 29));
        carBooking.returnCar();
        List<CarRentalInfo> shouldBeDeletedRecords = carBooking.getRentalInfoList().stream()
                .filter(carRentalInfo -> carRentalInfo.getTrackState() == TrackState.DELETED)
                .collect(Collectors.toList());
        assertThat(carBooking.getOrder().getState()).isEqualTo(OrderState.RETURNED_IN_ADVANCE);
        assertThat(shouldBeDeletedRecords.size()).isEqualTo(1);
    }

    @Test
    public void should_update_order_state_when_car_returned_in_time() {
        CarBooking carBooking = mockCarBooking();
        mockDate(LocalDate.of(2021, 8, 30));
        carBooking.returnCar();
        List<CarRentalInfo> shouldBeDeletedRecords = carBooking.getRentalInfoList().stream()
                .filter(carRentalInfo -> carRentalInfo.getTrackState() == TrackState.DELETED)
                .collect(Collectors.toList());
        assertThat(carBooking.getOrder().getState()).isEqualTo(OrderState.RETURNED_IN_TIME);
        assertThat(shouldBeDeletedRecords.size()).isEqualTo(0);
    }

    @Test
    public void should_rent_records_increase_when_car_returned_delayed() {
        CarBooking carBooking = mockCarBooking();
        mockDate(LocalDate.of(2021, 8, 31));
        carBooking.returnCar();
        List<CarRentalInfo> shouldBeAddedRecords = carBooking.getRentalInfoList().stream()
                .filter(carRentalInfo -> carRentalInfo.getTrackState() == TrackState.CREATED)
                .collect(Collectors.toList());
        assertThat(carBooking.getOrder().getState()).isEqualTo(OrderState.RETURNED_DELAYED);
        assertThat(shouldBeAddedRecords.size()).isEqualTo(1);
        assertThat(shouldBeAddedRecords.get(0).getRentalDate()).isEqualTo(LocalDate.of(2021,8,31));
    }

    @Test
    public void should_throw_exception_when_car_returned_twice() {
        CarBooking carBooking = mockCarBooking();
        mockDate(LocalDate.of(2021, 8, 31));
        carBooking.returnCar();
        String message = assertThrows(IllegalStateException.class,
                () -> carBooking.returnCar()).getMessage();
        assertThat(message).isEqualTo("The car has been returned before");
    }

    @Test
    public void should_cancel_failed_when_car_has_start() {
        CarBooking carBooking = mockCarBooking();
        carBooking.getOrder().setState(OrderState.EFFECTIVE);
        String message = assertThrows(IllegalStateException.class,
                () -> carBooking.cancelOrder()).getMessage();
        assertThat(message).isEqualTo("It can be cancelled 24 hours before the order starts！");
    }

    @Test
    public void should_cancel_failed_when_it_is_less_than_24_hours_to_start() {
        CarBooking carBooking = mockCarBooking();
        mockDate(LocalDate.of(2021,8,27));
        String message = assertThrows(IllegalStateException.class,
                () -> carBooking.cancelOrder()).getMessage();
        assertThat(message).isEqualTo("It can be cancelled 24 hours before the order starts！");
    }

    @Test
    public void should_cancel_successfully_when_it_is_more_than_24_hours_to_start() {
        CarBooking carBooking = mockCarBooking();
        mockDate(LocalDate.of(2021,8,26));
        carBooking.cancelOrder();
        List<CarRentalInfo> shouldBeDeleted = carBooking.getRentalInfoList().stream()
                .filter(carRentalInfo -> carRentalInfo.getTrackState() == TrackState.DELETED)
                .collect(Collectors.toList());
        assertThat(carBooking.getOrder().getState()).isEqualTo(OrderState.CANCELED);
        assertThat(shouldBeDeleted.size()).isEqualTo(3);
    }

    @Test
    public void should_retrieve_successfully_when_retrieved_date_is_between_start_date_and_end_date() {
        CarBooking carBooking = mockCarBooking();
        mockDate(LocalDate.of(2021,8,29));
        carBooking.startOrder();
        assertThat(carBooking.getOrder().getState()).isEqualTo(OrderState.EFFECTIVE);
    }

    @Test
    public void should_retrieve_failed_when_retrieved_is_after_end_date() {
        CarBooking carBooking = mockCarBooking();
        mockDate(LocalDate.of(2021,8,31));
        String message = assertThrows(IllegalStateException.class,
                () -> carBooking.startOrder()).getMessage();
        assertThat(message).isEqualTo("The order is not start or ended");
    }

    @Test
    public void should_retrieve_failed_when_the_order_has_start() {
        CarBooking carBooking = mockCarBooking();
        carBooking.getOrder().setState(OrderState.EFFECTIVE);
        mockDate(LocalDate.of(2021,8,29));
        String message = assertThrows(IllegalStateException.class,
                () -> carBooking.startOrder()).getMessage();
        assertThat(message).isEqualTo("The order has started");
    }

    protected void mockDate(LocalDate localDate) {
        DateTimeProvider dateTimeProvider = mock(DateTimeProvider.class);
        when(dateTimeProvider.nowDate()).thenReturn(localDate);
        DateTime.set(dateTimeProvider);
    }

    private CarBooking mockCarBooking() {
        String bookingId = UUID.randomUUID().toString();
        BookingOrder order = new BookingOrder(bookingId, "customerId", OrderState.BOOKED,
                LocalDate.of(2021, 8, 28), LocalDate.of(2021, 8, 30), null);
        List<CarRentalInfo> carRentalInfoList = new ArrayList<>();
        carRentalInfoList.add(new CarRentalInfo("car1", LocalDate.of(2021, 8, 28), bookingId));
        carRentalInfoList.add(new CarRentalInfo("car1", LocalDate.of(2021, 8, 29), bookingId));
        carRentalInfoList.add(new CarRentalInfo("car1", LocalDate.of(2021, 8, 30), bookingId));
        return new CarBooking(bookingId, order, carRentalInfoList);
    }
}