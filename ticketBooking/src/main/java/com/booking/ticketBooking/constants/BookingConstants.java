package com.booking.ticketBooking.constants;

public class BookingConstants {

    private BookingConstants(){

    }

    public static final String BOOKING_STATUS_PENDING = "PENDING_PAYMENT";
    public static final String SHOW_STATUS_ACTIVE = "A";
    public static final String ILLEGAL_BOOKING_EXCEPTION = "Illegal Booking Request";
    public static final String SEATS_UNAVAILABLE_EXCEPTION = "Seats Unavailable";
    public static final String SERVICE_DOWN = "Service Unavailable. Please Contact Administrator";
    public static final String BOOKING_NOT_FOUND = "Booking not found";

    public static final String PAYMENT_SUCCESS_TOPIC = "payment-succeeded";

    public static final String PAYMENT_EXPIRE_TOPIC = "payment-expired";

}
