package com.booking.payments.service;

import com.booking.payments.constants.PaymentsConstants;
import com.booking.payments.dto.ProcessingDto;
import com.booking.payments.entity.PaymentPollerEntity;
import com.booking.payments.repository.PaymentPollerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PaymentOutboxServiceImpl implements PaymentOutboxService{

    private PaymentPollerRepository paymentPollerRepository;


    @Override
    @Transactional
    public void publishPaymentSuccessEvent(ProcessingDto processingDto) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        //kafkaTemplate.send(PaymentsConstants.PAYMENT_SUCCESS_TOPIC,processingDto);
        boolean exists = paymentPollerRepository.existsByPaymentIdAndBookingId(processingDto.getPaymentId(), processingDto.getBookingId());

        if(exists){
            return;
        }
        PaymentPollerEntity paymentPollerEntity = new PaymentPollerEntity();
        paymentPollerEntity.setEventType(PaymentsConstants.PAYMENT_CONFIRMED);
        paymentPollerEntity.setPaymentId(processingDto.getPaymentId());
        paymentPollerEntity.setBookingId(processingDto.getBookingId());
        paymentPollerEntity.setProcessed(false);
        paymentPollerEntity.setTopic(PaymentsConstants.PAYMENT_SUCCESS_TOPIC);
        paymentPollerEntity.setPayload(objectMapper.writeValueAsString(processingDto));
        paymentPollerEntity.setCreatedAt(LocalDateTime.now());

        paymentPollerRepository.save(paymentPollerEntity);

    }

    @Override
    @Transactional
    public void publishPaymentExpiredEvent(ProcessingDto processingDto) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        boolean exists = paymentPollerRepository.existsByPaymentIdAndBookingId(processingDto.getPaymentId(), processingDto.getBookingId());

        if(exists){
            return;
        }
        PaymentPollerEntity paymentPollerEntity = new PaymentPollerEntity();
        paymentPollerEntity.setEventType(PaymentsConstants.PAYMENT_FAILED);
        paymentPollerEntity.setPaymentId(processingDto.getPaymentId());
        paymentPollerEntity.setBookingId(processingDto.getBookingId());
        paymentPollerEntity.setProcessed(false);
        paymentPollerEntity.setTopic(PaymentsConstants.PAYMENT_EXPIRE_TOPIC);
        paymentPollerEntity.setPayload(objectMapper.writeValueAsString(processingDto));
        paymentPollerEntity.setCreatedAt(LocalDateTime.now());

        paymentPollerRepository.save(paymentPollerEntity);

        //kafkaTemplate.send(PaymentsConstants.PAYMENT_EXPIRE_TOPIC,processingDto);

    }
}
