package com.booking.ticketBooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private String bookingId;

    private String showId;

    private String movieId;

    private List<SeatDto> seatIdList;

    private String bookingStatus;

}
