package com.booking.ticketBooking.constants;

public class BookingConstants {

    private BookingConstants(){

    }

    public static final String BOOKING_STATUS_PENDING = "PENDING_PAYMENT";
    public static final String BOOKING_STATUS_PENDING_CONFIRMATION = "PENDING_CONFIRMATION";
    public static final String BOOKING_STATUS_FAILED = "BOOKING_FAILED";
    public static final String BOOKING_STATUS_SUCCESS = "BOOKING_CONFIRMED";
    public static final String BOOKING_NOT_EXISTS_EVENT = "NO_BOOKING";
    public static final String BOOKING_PENDING_CONFIRMATION_EVENT = "BOOKING_CONFIRMED";
    public static final String ILLEGAL_BOOKING_EXCEPTION = "Illegal Booking Request";
    public static final String SEATS_UNAVAILABLE_EXCEPTION = "Seats Unavailable";
    public static final String SERVICE_DOWN = "Service Unavailable. Please Contact Administrator";
    public static final String BOOKING_NOT_FOUND = "Booking not found";
    public static final String PAYMENT_SUCCESS_TOPIC = "payment-succeed";
    public static final String PAYMENT_EXPIRE_TOPIC = "payment-expire";
    public static final String BOOKING_SUCCESS_TOPIC = "booking-confirm";
    public static final String BOOKING_FAILED_TOPIC = "booking-fail";
    public static final String SEAT_CONFIRMED_TOPIC = "seat-confirm";
    public static final String SEAT_FAILED_TOPIC = "seat-fail";
    public static final String SEAT_RELEASE_TOPIC = "seat-release";


}
