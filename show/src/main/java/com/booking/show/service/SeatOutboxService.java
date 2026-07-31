package com.booking.show.service;

import com.booking.show.dto.ProcessingDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface SeatOutboxService {

    void publishSeatConfirmedSuccessEvent(ProcessingDto processingDto) throws JsonProcessingException;

    void publishSeatConfirmFailedEvent(ProcessingDto processingDto) throws JsonProcessingException;

}
