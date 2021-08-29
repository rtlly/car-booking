package com.demo.carbooking.adapter.persistence;

import com.demo.carbooking.domain.CarModel;
import com.demo.carbooking.domain.CarRentalInfo;
import com.demo.carbooking.domain.CarType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CarModelMapper {
    List<CarModel> findByType(CarType type);

    Optional<CarModel> finById(String carId);

    List<CarModel> findAll();
}
