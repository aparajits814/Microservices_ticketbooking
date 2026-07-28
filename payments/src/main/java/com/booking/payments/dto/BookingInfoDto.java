package com.booking.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingInfoDto {

    private String bookingId;

    private String showId;

    private BigDecimal price;

    private String bookingStatus;

}
