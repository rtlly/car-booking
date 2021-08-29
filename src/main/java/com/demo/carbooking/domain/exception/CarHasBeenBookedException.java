package com.demo.carbooking.domain.exception;



import com.demo.carbooking.domain.concept.Entity;

import java.util.UUID;

public class CarHasBeenBookedException extends DomainException {
    public CarHasBeenBookedException(String message) {
        super(message);
    }

    public <T extends Entity> CarHasBeenBookedException(Class<T> entityClass, UUID id) {
        super("the car has been booked already");
    }
}
