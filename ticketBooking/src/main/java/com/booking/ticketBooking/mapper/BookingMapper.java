package com.booking.ticketBooking.mapper;

import com.booking.ticketBooking.constants.BookingConstants;
import com.booking.ticketBooking.dto.BookingDto;
import com.booking.ticketBooking.dto.SeatDto;
import com.booking.ticketBooking.entity.BookingEntity;
import com.booking.ticketBooking.entity.BookingSeatEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingMapper {

    private BookingMapper(){

    }

    public static BookingEntity mapBookingDtoToBookingEntity(BookingDto bookingDto){

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setUserId("SYSTEM");
        bookingEntity.setBookingStatus(BookingConstants.BOOKING_STATUS_PENDING);
        bookingEntity.setCreatedAt(LocalDateTime.now());
        bookingEntity.setShowId(bookingDto.getShowId());

        return bookingEntity;

    }

    public static List<BookingSeatEntity> mapListBookingIdToBookingSeatEntity(BookingDto bookingDto){
        List<BookingSeatEntity> bookingSeatEntityList = new ArrayList<>();

        for(SeatDto seatDto:bookingDto.getSeatIdList()){
            BookingSeatEntity bookingSeatEntity = new BookingSeatEntity();
            bookingSeatEntity.setBookingId(bookingDto.getBookingId());
            bookingSeatEntity.setBookingId(bookingDto.getBookingId());
            bookingSeatEntity.setSeatId(seatDto.getSeatId());
            bookingSeatEntityList.add(bookingSeatEntity);
        }

        return bookingSeatEntityList;

    }

}
