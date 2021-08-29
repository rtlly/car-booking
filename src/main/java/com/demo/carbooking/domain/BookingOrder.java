package com.demo.carbooking.domain;

import com.demo.carbooking.common.util.DateTime;
import com.demo.carbooking.common.util.LocalDateUtil;
import com.demo.carbooking.domain.concept.TrackDomainObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingOrder extends TrackDomainObject {
    private String id;
    private String customerId;
    private OrderState state;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate returnedDate;

    public void update() {
        this.returnedDate = DateTime.toDate();
        if (LocalDateUtil.isBefore(returnedDate, endDate)) {
            this.state = OrderState.RETURNED_IN_ADVANCE;
        } else if (returnedDate.isEqual(endDate)){
            this.state = OrderState.RETURNED_IN_TIME;
        } else {
            this.state = OrderState.RETURNED_DELAYED;
        }
    }

    public void cancel() {
        this.state = OrderState.CANCELED;
    }

    public void start() {
        this.state = OrderState.EFFECTIVE;
    }
}
