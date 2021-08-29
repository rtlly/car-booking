package com.demo.carbooking.domain;

import com.demo.carbooking.common.util.DateTime;
import com.demo.carbooking.common.util.LocalDateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CarBooking {
    private String bookingId;
    private BookingOrder order;
    private List<CarRentalInfo> rentalInfoList;

    public boolean shouldReturnButNotReturnUntilNow() {
        return LocalDateUtil.isBefore(order.getEndDate(), DateTime.toDate()) && Objects.isNull(order.getReturnedDate());
    }

    public static CarBooking create(String carId, LocalDate startDate, LocalDate endDate, String customerId) {
        String bookingId = UUID.randomUUID().toString();
        BookingOrder order = new BookingOrder(bookingId, customerId, OrderState.BOOKED, startDate, endDate, null);
        int rentDays = LocalDateUtil.betweenDayAbs(startDate, endDate);
        List<CarRentalInfo> carRentalInfos = new ArrayList<>();
        for (int i = 0; i <= rentDays; ++i) {
            carRentalInfos.add(new CarRentalInfo(carId, startDate.plusDays(i), bookingId));
        }
        return new CarBooking(bookingId, order, carRentalInfos);
    }

    public void returnCar() {
        if (order.getState() == OrderState.BOOKED || order.getState() == OrderState.EFFECTIVE) {
            order.update();
            if (order.getState() == OrderState.RETURNED_DELAYED) {
                int delayedDays = LocalDateUtil.betweenDayAbs(order.getEndDate(), order.getReturnedDate());
                String carId = this.rentalInfoList.stream().findFirst().map(CarRentalInfo::getCarId).get();
                for (int i = 1; i <= delayedDays; ++i) {
                    CarRentalInfo delayedCarRentalInfo = new CarRentalInfo(carId, order.getEndDate().plusDays(i), this.bookingId);
                    delayedCarRentalInfo.markAsCreated();
                    this.rentalInfoList.add(delayedCarRentalInfo);
                }
            } else if (order.getState() == OrderState.RETURNED_IN_ADVANCE) {
                this.rentalInfoList.stream()
                        .filter(carRentalInfo -> carRentalInfo.getRentalDate().isAfter(order.getReturnedDate()))
                        .forEach(CarRentalInfo::markAsDeleted);
            }
        } else {
            throw new IllegalStateException("The car has been returned before");
        }
    }

    public void cancelOrder() {
        if (order.getState() != OrderState.BOOKED || LocalDateUtil.isAfterOrEqual(DateTime.toDate(), order.getStartDate().minusDays(1))) {
            throw new IllegalStateException("It can be cancelled 24 hours before the order starts！");
        }
        order.cancel();
        this.rentalInfoList.forEach(CarRentalInfo::markAsDeleted);
    }

    public void startOrder() {
        if (order.getState() != OrderState.BOOKED) {
            throw new IllegalStateException("The order has started");
        } else if (!LocalDateUtil.isBetween(order.getStartDate(), order.getEndDate(), DateTime.toDate())) {
            throw new IllegalStateException("The order is not start or ended");
        } else {
            order.start();
        }
    }
}
