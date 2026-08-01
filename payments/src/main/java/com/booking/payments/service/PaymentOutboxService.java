package com.booking.payments.service;

import com.booking.payments.dto.ProcessingDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataIntegrityViolationException;

public interface PaymentOutboxService {

    void publishEvent(ProcessingDto processingDto, String eventType, String topic) throws IllegalStateException, DataIntegrityViolationException;


}
