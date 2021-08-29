package com.demo.carbooking.domain;

import com.demo.carbooking.domain.concept.ValueObject;

public enum OrderState implements ValueObject {
    BOOKED,
    EFFECTIVE,
    RETURNED_IN_TIME,
    RETURNED_IN_ADVANCE,
    RETURNED_DELAYED,
    ENDED,
    CANCELED;
}
