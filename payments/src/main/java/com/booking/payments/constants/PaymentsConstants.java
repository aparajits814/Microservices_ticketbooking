package com.booking.payments.constants;

public class PaymentsConstants {

    private PaymentsConstants(){

    }

    public static final String CHECKOUT_STATUS_SUCCESS = "CHECKOUT_SUCCESS";
    public static final String CHECKOUT_STATUS_FAILURE = "CHECKOUT_FAILURE";
    public static final String SERVICE_UNAVAILABLE = "Service is Unavailable. Please Contact Administrator";
    public static final String PAYMENT_CURRENCY = "INR";
    public static final String PAYMENT_INITIATED = "INITIATED";
    public static final String PAYMENT_CONFIRMED = "CONFIRMED";
    public static final String PAYMENT_REFUNDED = "REFUNDED";
    public static final String PAYMENT_FAILED = "FAILED";
    public static final String PAYMENT_CHECKOUT_FAILED = "CHECKOUT_FAILED";
    public static final String PAYMENT_DISPLAY = "Show Booking";
    public static final String CHECKOUT_COMPLETED = "checkout.session.completed";
    public static final String CHECKOUT_EXPIRED = "checkout.session.expired";
    public static final String PAYMENT_SUCCESS_TOPIC = "payment-succeed";
    public static final String PAYMENT_EXPIRE_TOPIC = "payment-expire";
    public static final String BOOKING_FAILED_TOPIC = "booking-fail";


}
