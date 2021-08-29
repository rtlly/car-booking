package com.demo.carbooking.domain;

import com.demo.carbooking.common.util.DateTime;
import com.demo.carbooking.common.util.DateTimeProvider;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookingOrderTest {
    @Test
    public void should_order_state_update_to_return_in_time_when_returned_date_is_equal_to_end_date() {
        BookingOrder order = mockOrder(LocalDate.of(2021,8,29), LocalDate.of(2021,8,29));
        mockDate(LocalDate.of(2021,8,29));
        order.update();
        assertThat(order.getState()).isEqualTo(OrderState.RETURNED_IN_TIME);
        assertThat(order.getReturnedDate()).isEqualTo(LocalDate.of(2021,8,29));
    }

    @Test
    public void should_order_state_update_to_return_in_advance_when_returned_date_is_before_end_date() {
        BookingOrder order = mockOrder(LocalDate.of(2021,8,29), LocalDate.of(2021,8,30));
        mockDate(LocalDate.of(2021,8,29));
        order.update();
        assertThat(order.getState()).isEqualTo(OrderState.RETURNED_IN_ADVANCE);
        assertThat(order.getReturnedDate()).isEqualTo(LocalDate.of(2021,8,29));
    }

    @Test
    public void should_order_state_update_to_return_delayed_when_returned_date_is_after_end_date() {
        BookingOrder order = mockOrder(LocalDate.of(2021,8,29), LocalDate.of(2021,8,29));
        mockDate(LocalDate.of(2021,8,30));
        order.update();
        assertThat(order.getState()).isEqualTo(OrderState.RETURNED_DELAYED);
        assertThat(order.getReturnedDate()).isEqualTo(LocalDate.of(2021,8,30));
    }

    protected void mockDate(LocalDate localDate) {
        DateTimeProvider dateTimeProvider = mock(DateTimeProvider.class);
        when(dateTimeProvider.nowDate()).thenReturn(localDate);
        DateTime.set(dateTimeProvider);
    }

    private BookingOrder mockOrder(LocalDate startDate, LocalDate endDate) {
        return new BookingOrder(null, null, null,startDate, endDate, null);
    }
}