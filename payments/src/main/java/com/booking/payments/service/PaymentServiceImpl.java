package com.booking.payments.service;

import com.booking.payments.constants.PaymentsConstants;
import com.booking.payments.dto.ProcessingDto;
import com.booking.payments.entity.IdempotencyEntity;
import com.booking.payments.entity.PaymentEntity;
import com.booking.payments.repository.IdempotencyRepository;
import com.booking.payments.repository.PaymentsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private PaymentsRepository paymentsRepository;

    private PaymentOutboxService paymentOutboxService;

    private IdempotencyRepository idempotencyRepository;

    @Override
    @Transactional
    public void paymentSuccess(String sessionId, String sessionPaymentStatus) {

        Optional<PaymentEntity> paymentEntityOptional = paymentsRepository.findByStripeCheckoutSessionId(sessionId);

        if(paymentEntityOptional.isEmpty()){
            return;
        }

        String paymentStatus = paymentEntityOptional.get().getPaymentStatus();
        System.out.println("payment Status:"+paymentStatus);

        if(!paymentStatus.equalsIgnoreCase(PaymentsConstants.PAYMENT_INITIATED)){
            return;
        }

        PaymentEntity paymentEntity = paymentEntityOptional.get();

        paymentEntity.setPaymentStatus(PaymentsConstants.PAYMENT_CONFIRMED);

        paymentsRepository.save(paymentEntity);

        ProcessingDto processingDto = new ProcessingDto();
        processingDto.setBookingId(paymentEntity.getBookingId());
        processingDto.setPaymentId(paymentEntity.getPaymentId());
        processingDto.setEventType(PaymentsConstants.PAYMENT_SUCCESS_TOPIC);

        paymentOutboxService.publishEvent(processingDto,PaymentsConstants.PAYMENT_SUCCESS_TOPIC, PaymentsConstants.PAYMENT_SUCCESS_TOPIC);

    }

    @Override
    @Transactional
    public void paymentExpired(String sessionId) {

        Optional<PaymentEntity> paymentEntityOptional = paymentsRepository.findByStripeCheckoutSessionId(sessionId);

        if(paymentEntityOptional.isEmpty()){
            return;
        }

        String paymentStatus = paymentEntityOptional.get().getPaymentStatus();

        if(!paymentStatus.equalsIgnoreCase(PaymentsConstants.PAYMENT_INITIATED)){
            return;
        }

        PaymentEntity paymentEntity = paymentEntityOptional.get();

        paymentEntity.setPaymentStatus(PaymentsConstants.PAYMENT_FAILED);

        paymentsRepository.save(paymentEntity);

        ProcessingDto processingDto = new ProcessingDto();
        processingDto.setBookingId(paymentEntity.getBookingId());
        processingDto.setPaymentId(paymentEntity.getPaymentId());
        processingDto.setEventType(PaymentsConstants.PAYMENT_EXPIRE_TOPIC);

        paymentOutboxService.publishEvent(processingDto, PaymentsConstants.PAYMENT_EXPIRE_TOPIC, PaymentsConstants.PAYMENT_EXPIRE_TOPIC);

    }

    @Override
    @Transactional
    @KafkaListener(topics = PaymentsConstants.BOOKING_FAILED_TOPIC)
    public void processFailedBookingEvent(ProcessingDto processingDto) {

        IdempotencyEntity idempotencyEntity = new IdempotencyEntity();

        idempotencyEntity.setBookingId(processingDto.getBookingId());
        idempotencyEntity.setPaymentId(processingDto.getPaymentId());
        idempotencyEntity.setEventType(processingDto.getEventType());

        idempotencyRepository.save(idempotencyEntity);

        Optional<PaymentEntity> paymentEntityOptional = paymentsRepository.findById(processingDto.getPaymentId());

        PaymentEntity paymentEntity = paymentEntityOptional.get();

        paymentEntity.setPaymentStatus(PaymentsConstants.PAYMENT_REFUNDED);

        paymentsRepository.save(paymentEntity);
        //Notification

    }
}
