package com.demo.carbooking.adapter.rest;

import com.demo.carbooking.domain.CarModel;
import com.demo.carbooking.domain.CarType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CarOverviewInfo {
    private CarType carType;
    private BigDecimal rentPrice;

    public static CarOverviewInfo from(CarModel carModel) {
        return new CarOverviewInfo(carModel.getType(), carModel.getRentPrice());
    }
}
