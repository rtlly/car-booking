package com.demo.carbooking.domain;

import com.demo.carbooking.common.util.LocalDateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    CarModel carModel;
    List<CarBooking> bookingList;

    public boolean isCarAvailableBetween(LocalDate startDate, LocalDate endDate) {
        if (CollectionUtils.isEmpty(bookingList)) {
            return true;
        }

        List<CarBooking> delayedBooking = bookingList.stream()
                .filter(CarBooking::shouldReturnButNotReturnUntilNow)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(delayedBooking)) {
            return !this.bookingList.stream()
                    .flatMap(booking -> booking.getRentalInfoList().stream())
                    .anyMatch(carRentalInfo -> LocalDateUtil.isBetween(startDate, endDate, carRentalInfo.getRentalDate()));
        } else {
            return false;
        }
    }
}
