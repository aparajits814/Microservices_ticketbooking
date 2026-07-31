package com.booking.ticketBooking.service;

import com.booking.ticketBooking.constants.BookingConstants;
import com.booking.ticketBooking.dto.ProcessingDto;
import com.booking.ticketBooking.entity.BookingCompensationOutboxEntity;
import com.booking.ticketBooking.entity.BookingSeatOutboxEntity;
import com.booking.ticketBooking.repository.BookingCompensationOutboxRepository;
import com.booking.ticketBooking.repository.BookingSeatOutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class BookingOutboxServiceImpl implements BookingOutboxService{

    private BookingSeatOutboxRepository bookingSeatOutboxRepository;

    private BookingCompensationOutboxRepository bookingCompensationOutboxRepository;

    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public void publishBookingSuccessEvent(ProcessingDto processingDto, String eventType) throws JsonProcessingException {

        boolean exists = bookingSeatOutboxRepository.
                existsByBookingIdAndPaymentId(processingDto.getBookingId(), processingDto.getPaymentId());
        if(exists){
            return;
        }

        String payload = objectMapper.writeValueAsString(processingDto);

        BookingSeatOutboxEntity bookingOutboxEntity = new BookingSeatOutboxEntity();
        bookingOutboxEntity.setBookingId(processingDto.getBookingId());
        bookingOutboxEntity.setPaymentId(processingDto.getPaymentId());
        bookingOutboxEntity.setEventType(eventType);
        bookingOutboxEntity.setPayload(payload);
        bookingOutboxEntity.setProcessed(false);
        bookingOutboxEntity.setCreatedAt(LocalDateTime.now());
        bookingOutboxEntity.setTopic(BookingConstants.BOOKING_SUCCESS_TOPIC);
        bookingSeatOutboxRepository.save(bookingOutboxEntity);

    }

    @Override
    @Transactional
    public void publishBookingFailedEvent(ProcessingDto processingDto, String eventType) throws JsonProcessingException {

        boolean exists = bookingSeatOutboxRepository.
                existsByBookingIdAndPaymentId(processingDto.getBookingId(), processingDto.getPaymentId());
        if(exists){
            return;
        }

        String payload = objectMapper.writeValueAsString(processingDto);
        BookingCompensationOutboxEntity bookingCompensationOutboxEntity = new BookingCompensationOutboxEntity();
        bookingCompensationOutboxEntity.setBookingId(processingDto.getBookingId());
        bookingCompensationOutboxEntity.setPaymentId(processingDto.getPaymentId());
        bookingCompensationOutboxEntity.setEventType(eventType);
        bookingCompensationOutboxEntity.setPayload(payload);
        bookingCompensationOutboxEntity.setProcessed(false);
        bookingCompensationOutboxEntity.setCreatedAt(LocalDateTime.now());
        bookingCompensationOutboxEntity.setTopic(BookingConstants.BOOKING_FAILED_TOPIC);
        bookingCompensationOutboxRepository.save(bookingCompensationOutboxEntity);

    }
}
