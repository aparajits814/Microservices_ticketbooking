package com.booking.payments.service;

import com.booking.payments.dto.ProcessingDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaymentOutboxService {

    void publishPaymentSuccessEvent(ProcessingDto processingDto) throws JsonProcessingException;

    void publishPaymentExpiredEvent(ProcessingDto processingDto) throws JsonProcessingException;

}
