package com.booking.payments.service;

import com.booking.payments.constants.PaymentsConstants;
import com.booking.payments.dto.ProcessingDto;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class PaymentListener {

    private PaymentService paymentService;

    @KafkaListener(topics = PaymentsConstants.BOOKING_FAILED_TOPIC)
    public void processFailedBookingEvent(ProcessingDto processingDto) {

        paymentService.processFailedBookingEvent(processingDto);

    }

}
