package com.booking.payments.service;

import com.booking.payments.dto.ProcessingDto;

public interface PaymentService {

    void paymentSuccess(String sessionId,String sessionPaymentStatus, String paymentIntentId);

    void paymentExpired(String sessionId);

    void processFailedBookingEvent(ProcessingDto processingDto);

}
