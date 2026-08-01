package com.booking.ticketBooking.service;


import com.booking.ticketBooking.dto.ProcessingDto;
import com.booking.ticketBooking.entity.BookingSeatOutboxEntity;
import com.booking.ticketBooking.repository.BookingSeatOutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class BookingOutboxServiceImpl implements BookingOutboxService{

    private BookingSeatOutboxRepository bookingSeatOutboxRepository;

    private ObjectMapper objectMapper;

    @Override
    public void publishEvent(ProcessingDto processingDto, String eventType, String topic) throws IllegalStateException, DataIntegrityViolationException {
        String payload;
        try{
            payload = objectMapper.writeValueAsString(processingDto);
        }catch(JsonProcessingException e) {
            throw new IllegalStateException();
        }

        BookingSeatOutboxEntity bookingOutboxEntity = new BookingSeatOutboxEntity();
        bookingOutboxEntity.setBookingId(processingDto.getBookingId());
        bookingOutboxEntity.setPaymentId(processingDto.getPaymentId());
        bookingOutboxEntity.setEventType(eventType);
        bookingOutboxEntity.setPayload(payload);
        bookingOutboxEntity.setProcessed(false);
        bookingOutboxEntity.setCreatedAt(LocalDateTime.now());
        bookingOutboxEntity.setTopic(topic);
        bookingSeatOutboxRepository.save(bookingOutboxEntity);
    }
}
