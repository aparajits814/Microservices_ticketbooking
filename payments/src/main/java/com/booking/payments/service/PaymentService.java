package com.booking.payments.service;

public interface PaymentService {

    void paymentSuccess(String sessionId,String sessionPaymentStatus);

    void paymentExpired(String sessionId);

}
