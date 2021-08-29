package com.demo.carbooking.adapter.persistence;


import com.demo.carbooking.domain.BookingOrder;
import com.demo.carbooking.domain.CarBooking;
import com.demo.carbooking.domain.CarBookingRepository;
import com.demo.carbooking.domain.CarRentalInfo;
import com.demo.carbooking.domain.OrderState;
import com.demo.carbooking.domain.concept.TrackState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CarBookingRepositoryImpl implements CarBookingRepository {
    private final BookingOrderMapper bookingOrderMapper;
    private final CarRentalInfoMapper carRentalInfoMapper;

    @Override
    @Transactional
    public void save(CarBooking carBooking) {
        bookingOrderMapper.save(carBooking.getOrder());
        carBooking.getRentalInfoList().forEach(carRentalInfoMapper::save);
    }

    @Override
    public List<CarBooking> findNotEndedBy(String carId) {
        List<CarRentalInfo> carRentalInfoList = carRentalInfoMapper.findBy(carId);
        return carRentalInfoList.stream().collect(Collectors.groupingBy(CarRentalInfo::getOrderId))
                .entrySet()
                .stream()
                .map(entry -> {
                    BookingOrder order = bookingOrderMapper.findById(entry.getKey())
                            .orElseThrow(() -> new IllegalStateException("order not exists"));
                    if (order.getState() == OrderState.BOOKED || order.getState() == OrderState.EFFECTIVE) {
                        return new CarBooking(order.getId(), order, entry.getValue());
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CarBooking> findByOrderId(String orderId) {
        Optional<BookingOrder> order = bookingOrderMapper.findById(orderId);
        if (order.isPresent()) {
            List<CarRentalInfo> carRentalInfoList = carRentalInfoMapper.findByOrderId(orderId);
            return Optional.of(new CarBooking(orderId, order.get(), carRentalInfoList));
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void updateBooking(CarBooking booking) {
        BookingOrder order = booking.getOrder();
        bookingOrderMapper.update(order);

        booking.getRentalInfoList().forEach(carRentalInfo -> {
            if (carRentalInfo.getTrackState() == TrackState.DELETED) {
                carRentalInfoMapper.delete(carRentalInfo.getCarId(), carRentalInfo.getRentalDate());
            } else if (carRentalInfo.getTrackState() == TrackState.CREATED) {
                carRentalInfoMapper.save(carRentalInfo);
            }
        });
    }
}
