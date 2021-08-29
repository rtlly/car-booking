package com.demo.carbooking.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CarModel {
    private String id;
    private CarType type;
    private String carNumber;
    private BigDecimal rentPrice;
}
