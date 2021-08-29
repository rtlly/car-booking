package com.demo.carbooking.adapter.persistence;

import com.demo.carbooking.domain.CarRentalInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CarRentalInfoMapper {
    List<CarRentalInfo> findBy(String carId);
    List<CarRentalInfo> findByOrderId(String orderId);
    void save(@Param("carRentalInfo") CarRentalInfo carRentalInfo);

    void delete(@Param("carId") String carId, @Param("rentalDate") LocalDate rentalDate);
}
