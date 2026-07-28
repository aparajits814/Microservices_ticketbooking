package com.booking.ticketBooking.exception;

public class BookingNotExistsException extends RuntimeException{

    public BookingNotExistsException(String message){
        super(message);
    }

}
