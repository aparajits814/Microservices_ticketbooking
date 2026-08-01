package com.booking.ticketBooking.service;

import com.booking.ticketBooking.dto.ProcessingDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataIntegrityViolationException;

public interface BookingOutboxService {

    void publishEvent(ProcessingDto processingDto, String eventType, String topic) throws IllegalStateException, DataIntegrityViolationException;


}
