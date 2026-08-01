package com.booking.show.constants;

public class ShowConstants {

    private ShowConstants(){

    }

    public static final String SHOW_EXISTS_MESSAGE = "Show with given ID already Exists";
    public static final String SHOW_STATUS_ACTIVE = "A";
    public static final String SHOW_DOES_NOT_EXISTS_MESSAGE = "Show with Given ID does not exists";
    public static final String SEAT_STATUS_AVALIABLE = "AVALIABLE";
    public static final String SEAT_STATUS_LOCKED = "LOCKED";
    public static final String SEAT_STATUS_BOOKED = "BOOKED";
    public static final String ILLEGAL_SHOW_EXCEPTION = "Illegal Show Request";
    public static final String SEAT_UNAVALIABLE_EXCEPTION = "Seat(s) are unavailable";
    public static final String BOOKING_SUCCESS_TOPIC = "booking-confirm";
    public static final String BOOKING_FAILED_TOPIC = "booking-fail";
    public static final String SEAT_CONFIRMED_TOPIC = "seat-confirm";
    public static final String SEAT_FAILED_TOPIC = "seat-fail";
    public static final String SEAT_RELEASE_TPOIC = "seat-release";

}
