package com.booking.show.service;

import com.booking.show.constants.ShowConstants;
import com.booking.show.dto.ProcessingDto;
import com.booking.show.entity.ShowSeatOutboxEntity;
import com.booking.show.repository.ShowSeatOutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SeatOutboxServiceImpl implements SeatOutboxService{

    private ShowSeatOutboxRepository showSeatOutboxRepository;

    private ObjectMapper objectMapper;


    @Override
    public void publishEvent(ProcessingDto processingDto, String eventType, String topic) throws IllegalStateException, DataIntegrityViolationException {
        String responsePayload;
        try {
            responsePayload = objectMapper.writeValueAsString(processingDto);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException();
        }

        ShowSeatOutboxEntity showSeatOutboxEntity = new ShowSeatOutboxEntity();
        showSeatOutboxEntity.setBookingId(processingDto.getBookingId());
        showSeatOutboxEntity.setPaymentId(processingDto.getPaymentId());
        showSeatOutboxEntity.setTopic(topic);
        showSeatOutboxEntity.setEventType(eventType);
        showSeatOutboxEntity.setProcessed(false);
        showSeatOutboxEntity.setCreatedAt(LocalDateTime.now());
        showSeatOutboxEntity.setPayload(responsePayload);
        showSeatOutboxRepository.save(showSeatOutboxEntity);
    }
}
