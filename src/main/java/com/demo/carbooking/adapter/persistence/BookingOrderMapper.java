package com.demo.carbooking.adapter.persistence;

import com.demo.carbooking.domain.BookingOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface BookingOrderMapper {
    Optional<BookingOrder> findByCustomerId(String customerId);
    Optional<BookingOrder> findById(@Param("orderId") String orderId);

    void save(@Param("order") BookingOrder order);

    void update(@Param("order")BookingOrder order);
}
