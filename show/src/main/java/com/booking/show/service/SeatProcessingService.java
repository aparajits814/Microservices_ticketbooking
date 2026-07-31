package com.booking.show.service;

import com.booking.show.dto.ProcessingDto;

public interface SeatProcessingService {

    void processBookingSuccessEvent(ProcessingDto processingDto);

    void processBookingFailedEvent(ProcessingDto processingDto);

}
