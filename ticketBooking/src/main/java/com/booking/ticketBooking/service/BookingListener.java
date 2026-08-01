package com.booking.ticketBooking.service;

import com.booking.ticketBooking.constants.BookingConstants;
import com.booking.ticketBooking.dto.ProcessingDto;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookingListener {

    private BookingProcessingService bookingProcessingService;

    @KafkaListener(topics = BookingConstants.PAYMENT_SUCCESS_TOPIC)
    public void processPaymentSuccessEvent(ProcessingDto processingDto) {

        bookingProcessingService.processPaymentSuccessEvent(processingDto);

    }

    @KafkaListener(topics = BookingConstants.PAYMENT_EXPIRE_TOPIC)
    public void processPaymentExpiredEvent(ProcessingDto processingDto) {

        bookingProcessingService.processPaymentExpiredEvent(processingDto);

    }

    @KafkaListener(topics = BookingConstants.SEAT_CONFIRMED_TOPIC)
    public void processSeatConfirmedEvent(ProcessingDto processingDto) {

        bookingProcessingService.processSeatConfirmedEvent(processingDto);

    }

    @KafkaListener(topics = BookingConstants.SEAT_FAILED_TOPIC)
    public void processSeatFailedEvent(ProcessingDto processingDto) {

        bookingProcessingService.processSeatFailedEvent(processingDto);

    }
}
