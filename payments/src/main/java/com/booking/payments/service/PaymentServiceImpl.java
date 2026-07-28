package com.booking.payments.service;

import com.booking.payments.constants.PaymentsConstants;
import com.booking.payments.dto.ProcessingDto;
import com.booking.payments.entity.PaymentEntity;
import com.booking.payments.repository.PaymentsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private PaymentsRepository paymentsRepository;

    private PaymentOutboxService paymentOutboxService;

    @Override
    @Transactional
    public void paymentSuccess(String sessionId, String sessionPaymentStatus) {

        Optional<PaymentEntity> paymentEntityOptional = paymentsRepository.findByStripeCheckoutSessionId(sessionId);

        if(paymentEntityOptional.isEmpty()){
            return;
        }

        String paymentStatus = paymentEntityOptional.get().getPaymentStatus();

        if(!paymentStatus.equalsIgnoreCase(PaymentsConstants.PAYMENT_INITIATED)){
            return;
        }

        PaymentEntity paymentEntity = paymentEntityOptional.get();

        paymentEntity.setPaymentStatus(PaymentsConstants.PAYMENT_CONFIRMED);

        paymentsRepository.save(paymentEntity);

        ProcessingDto processingDto = new ProcessingDto();
        processingDto.setBookingId(paymentEntity.getBookingId());
        processingDto.setPaymentId(paymentEntity.getPaymentId());
        processingDto.setPaymentStatus(paymentEntity.getPaymentStatus());

        try {

            paymentOutboxService.publishPaymentSuccessEvent(processingDto);

        }catch(JsonProcessingException e){

            throw new IllegalStateException(" ");

        }

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
        processingDto.setPaymentStatus(paymentEntity.getPaymentStatus());

        try {

            paymentOutboxService.publishPaymentExpiredEvent(processingDto);

        }catch(JsonProcessingException e){

            throw new IllegalStateException(" ");

        }

    }
}
