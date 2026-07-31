package com.booking.ticketBooking.service;

import com.booking.ticketBooking.dto.ProcessingDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface BookingOutboxService {

    void publishBookingSuccessEvent(ProcessingDto processingDto, String eventType) throws JsonProcessingException;

    void publishBookingFailedEvent(ProcessingDto processingDto, String eventType) throws JsonProcessingException;

}
