package com.booking.payments.service;

import com.booking.payments.constants.PaymentsConstants;
import com.booking.payments.dto.ProcessingDto;
import com.booking.payments.entity.PaymentPollerEntity;
import com.booking.payments.repository.PaymentPollerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PaymentOutboxServiceImpl implements PaymentOutboxService{

    private PaymentPollerRepository paymentPollerRepository;

    private ObjectMapper objectMapper;


    @Override
    public void publishEvent(ProcessingDto processingDto, String eventType, String topic) throws IllegalStateException, DataIntegrityViolationException {

        PaymentPollerEntity paymentPollerEntity = new PaymentPollerEntity();
        paymentPollerEntity.setEventType(eventType);
        paymentPollerEntity.setPaymentId(processingDto.getPaymentId());
        paymentPollerEntity.setBookingId(processingDto.getBookingId());
        paymentPollerEntity.setProcessed(false);
        paymentPollerEntity.setTopic(topic);
        try {
            paymentPollerEntity.setPayload(objectMapper.writeValueAsString(processingDto));
        }catch(JsonProcessingException e){
            throw new IllegalStateException();
        }
        paymentPollerEntity.setCreatedAt(LocalDateTime.now());

        paymentPollerRepository.save(paymentPollerEntity);
    }
}
