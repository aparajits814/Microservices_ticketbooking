package com.booking.ticketBooking.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingSeatId implements Serializable {

    private String seatId;

    private String bookingId;

}
