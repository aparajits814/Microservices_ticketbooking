package com.booking.ticketBooking.service;

import com.booking.ticketBooking.dto.BookingDto;
import com.booking.ticketBooking.dto.BookingInfoDto;

public interface BookingService {

    BookingDto createBooking(BookingDto bookingDto);

    BookingInfoDto findBooking(String bookingId);

}
