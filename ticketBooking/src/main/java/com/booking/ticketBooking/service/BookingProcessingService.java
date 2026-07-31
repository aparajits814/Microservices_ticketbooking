package com.booking.ticketBooking.service;

import com.booking.ticketBooking.dto.ProcessingDto;

public interface BookingProcessingService {

    void processPaymentSuccessEvent(ProcessingDto processingDto);

    void processPaymentExpiredEvent(ProcessingDto processingDto);

    void processSeatConfirmedEvent(ProcessingDto processingDto);

    void processSeatFailedEvent(ProcessingDto processingDto);

}
