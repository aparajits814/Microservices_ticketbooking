package com.booking.ticketBooking.service;

import com.booking.ticketBooking.dto.ProcessingDto;

public interface BookingConfirmationService {

    void processPaymentSuccessEvent(ProcessingDto processingDto);

    void processPaymentExpiredEvent(ProcessingDto processingDto);

}
