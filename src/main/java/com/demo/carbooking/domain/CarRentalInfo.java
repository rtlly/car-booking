package com.demo.carbooking.domain;

import com.demo.carbooking.domain.concept.TrackDomainObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarRentalInfo extends TrackDomainObject {
    private String carId;
    private LocalDate rentalDate;
    private String orderId;
}
