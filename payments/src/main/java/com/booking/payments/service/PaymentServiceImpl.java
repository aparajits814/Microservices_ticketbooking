package com.booking.payments.service;

import com.booking.payments.constants.PaymentsConstants;
import com.booking.payments.dto.ProcessingDto;
import com.booking.payments.entity.IdempotencyEntity;
import com.booking.payments.entity.PaymentEntity;
import com.booking.payments.repository.IdempotencyRepository;
import com.booking.payments.repository.PaymentsRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService{

    private PaymentsRepository paymentsRepository;

    private PaymentOutboxService paymentOutboxService;

    private IdempotencyRepository idempotencyRepository;

    @Override
    @Transactional
    public void paymentSuccess(String sessionId, String sessionPaymentStatus, String paymentIntentId) {

        log.info("Payment intent ID:{}",paymentIntentId);

        Optional<PaymentEntity> paymentEntityOptional = paymentsRepository.findByStripeCheckoutSessionId(sessionId);

        if(paymentEntityOptional.isEmpty()){
            return;
        }

        String paymentStatus = paymentEntityOptional.get().getPaymentStatus();
        log.info("payment Status:{}", paymentStatus);

        if(!paymentStatus.equalsIgnoreCase(PaymentsConstants.PAYMENT_INITIATED)){
            return;
        }

        PaymentEntity paymentEntity = paymentEntityOptional.get();

        paymentEntity.setPaymentStatus(PaymentsConstants.PAYMENT_CONFIRMED);
        paymentEntity.setStripePaymentIntent(paymentIntentId);

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
    public void processFailedBookingEvent(ProcessingDto processingDto) {
        log.info("Inside process failed booking event");

        IdempotencyEntity idempotencyEntity = new IdempotencyEntity();

        idempotencyEntity.setBookingId(processingDto.getBookingId());
        idempotencyEntity.setPaymentId(processingDto.getPaymentId());
        idempotencyEntity.setEventType(processingDto.getEventType());

        idempotencyRepository.save(idempotencyEntity);

        Optional<PaymentEntity> paymentEntityOptional = paymentsRepository.findById(processingDto.getPaymentId());

        if(paymentEntityOptional.isEmpty()){
            return;
        }

        PaymentEntity paymentEntity = paymentEntityOptional.get();

        log.info("Going to refund the payment");

        try {
            refundPayment(paymentEntity);
        } catch (StripeException e) {
            log.info("Refund failed");
            throw new IllegalStateException();
        }
        //Notification

    }

    private void refundPayment(PaymentEntity paymentEntity) throws StripeException{

        RefundCreateParams params =
                RefundCreateParams.builder()
                        .setPaymentIntent(
                                paymentEntity.getStripePaymentIntent()
                        )
                        .build();

        Refund refund = Refund.create(params);

        if ("succeeded".equals(refund.getStatus())) {
            paymentEntity.setPaymentStatus(PaymentsConstants.PAYMENT_REFUNDED);
            paymentsRepository.save(paymentEntity);
        }
        log.info("Refunded");
    }
}
