package com.booking.show.service;

import com.booking.show.dto.ProcessingDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataIntegrityViolationException;

public interface SeatOutboxService {

    void publishEvent(ProcessingDto processingDto, String eventType, String topic) throws IllegalStateException, DataIntegrityViolationException;

}
