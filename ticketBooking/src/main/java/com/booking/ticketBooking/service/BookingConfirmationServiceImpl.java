package com.booking.ticketBooking.service;

import com.booking.ticketBooking.constants.BookingConstants;
import com.booking.ticketBooking.dto.ProcessingDto;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class BookingConfirmationServiceImpl implements BookingConfirmationService{

    @Override
    @KafkaListener(topics = BookingConstants.PAYMENT_SUCCESS_TOPIC)
    @Transactional
    public void processPaymentSuccessEvent(ProcessingDto processingDto) {

    }

    @Override
    @KafkaListener(topics = BookingConstants.PAYMENT_EXPIRE_TOPIC)
    @Transactional
    public void processPaymentExpiredEvent(ProcessingDto processingDto) {

    }
}
