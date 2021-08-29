package com.demo.carbooking.application;

import com.demo.carbooking.adapter.persistence.CarModelMapper;
import com.demo.carbooking.adapter.rest.CarBookingDetailResponseDto;
import com.demo.carbooking.adapter.rest.CarOverviewInfo;
import com.demo.carbooking.adapter.rest.CarRentalRequestDto;
import com.demo.carbooking.common.util.DateTime;
import com.demo.carbooking.common.util.LocalDateUtil;
import com.demo.carbooking.domain.Car;
import com.demo.carbooking.domain.CarBooking;
import com.demo.carbooking.domain.CarBookingRepository;
import com.demo.carbooking.domain.CarModel;
import com.demo.carbooking.domain.CarRentalInfo;
import com.demo.carbooking.domain.exception.CarHasBeenBookedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarBookingService {
    private final CarBookingRepository carBookingRepository;
    private final CarModelMapper carModelMapper;

    public String rent(CarRentalRequestDto carRentalRequest) throws Exception {
        //TODO 待解决两个人同时预定一个车型时，其中一个人会不成功，加锁会影响效率
        LocalDate now = DateTime.toDate();
        if (LocalDateUtil.isBefore(carRentalRequest.getEndDate(), now)
                || LocalDateUtil.isBefore(carRentalRequest.getStartDate(), now)) {
            throw new Exception("can not book the date before today");
        }

        List<CarModel> carModels = carModelMapper.findByType(carRentalRequest.getCarType());
        Car availableCar = carModels.stream()
                .map(carModel -> {
                    List<CarBooking> carBookingList = carBookingRepository.findNotEndedBy(carModel.getId());
                    return new Car(carModel, carBookingList);
                })
                .filter(car -> car.isCarAvailableBetween(carRentalRequest.getStartDate(), carRentalRequest.getEndDate()))
                .findAny()
                .orElseThrow(() -> new CarHasBeenBookedException("car has been booked"));
        CarBooking carBooking = CarBooking.create(availableCar.getCarModel().getId(),
                carRentalRequest.getStartDate(),
                carRentalRequest.getEndDate(),
                carRentalRequest.getCustomerId());
        try {
            carBookingRepository.save(carBooking);
        } catch (Exception e) {
            throw new CarHasBeenBookedException("car has been booked");
        }
        return carBooking.getBookingId();
    }

    public void returnCar(String orderId) {
        CarBooking booking = carBookingRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalStateException("order is not exists"));
        booking.returnCar();
        carBookingRepository.updateBooking(booking);
    }

    public void cancelOrder(String orderId) {
        CarBooking booking = carBookingRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalStateException("order is not exists"));
        booking.cancelOrder();
        carBookingRepository.updateBooking(booking);
    }

    public void retrieveCar(String orderId) {
        //TODO 后续需要加上定时任务把一直没有人取车的订单，在到期后更新成结束
        CarBooking booking = carBookingRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalStateException("order is not exists"));
        booking.startOrder();
        carBookingRepository.updateBooking(booking);
    }

    public CarBookingDetailResponseDto getBookingDetail(String orderId) {
        CarBooking booking = carBookingRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalStateException("order is not exists"));
        String carId = booking.getRentalInfoList().stream().findFirst().map(CarRentalInfo::getCarId).get();
        CarModel carModel = carModelMapper.finById(carId).orElseThrow(() -> new IllegalStateException("car is not existed"));
        return new CarBookingDetailResponseDto(carModel, booking);
    }

    public List<CarOverviewInfo> getAllAvailableCarsBetweenTheseDays(LocalDate startDate, LocalDate endDate) {
        List<CarModel> carModels = carModelMapper.findAll();
        List<CarModel> availableCars = carModels.stream()
                .map(carModel -> {
                    List<CarBooking> carBookingList = carBookingRepository.findNotEndedBy(carModel.getId());
                    return new Car(carModel, carBookingList);
                })
                .filter(car -> car.isCarAvailableBetween(startDate, endDate))
                .map(Car::getCarModel)
                .collect(Collectors.toList());
        return availableCars.stream().map(CarOverviewInfo::from)
                .distinct()
                .collect(Collectors.toList());
    }
}
