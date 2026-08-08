package com.booking.payments.service;

import com.booking.payments.configuration.StripeProperties;
import com.booking.payments.constants.PaymentsConstants;
import com.booking.payments.dto.BookingInfoDto;
import com.booking.payments.dto.PaymentCheckoutDto;
import com.booking.payments.dto.PaymentResponseDto;
import com.booking.payments.entity.PaymentEntity;
import com.booking.payments.exception.ResourceNotFoundException;
import com.booking.payments.mapper.PaymentsMapper;
import com.booking.payments.repository.PaymentsRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentCheckoutServiceImpl implements PaymentCheckoutService{

    private BookingFeignClient bookingFeignClient;

    private final StripeProperties stripeProperties;

    private PaymentsRepository paymentsRepository;

    @Override
    @Transactional
    public PaymentResponseDto createPaymentCheckout(PaymentCheckoutDto paymentCheckoutDto) {

        String paymentId = UUID.randomUUID().toString();

        BookingInfoDto bookingInfoDto;

        try {

            bookingInfoDto = bookingFeignClient.findBookingById(paymentCheckoutDto.getBookingId()).getBody();

        }catch(FeignException.ServiceUnavailable exception){

            throw new ResourceNotFoundException(PaymentsConstants.SERVICE_UNAVAILABLE);

        }catch(FeignException.BadRequest exception){

            throw new ResourceNotFoundException(" ");
        }

        Optional<PaymentEntity> paymentEntityOptional = paymentsRepository.findByBookingId(paymentCheckoutDto.getBookingId());



        if(paymentEntityOptional.isPresent()){

            if(PaymentsConstants.PAYMENT_CHECKOUT_FAILED.equals(paymentEntityOptional.get().getPaymentStatus())){

                return new PaymentResponseDto(paymentEntityOptional.get().getBookingId(),
                        "","","", PaymentsConstants.CHECKOUT_STATUS_FAILURE);

            }else{

                return new PaymentResponseDto(paymentEntityOptional.get().getBookingId(),
                        paymentEntityOptional.get().getStripeCheckoutSessionId()
                        ,paymentEntityOptional.get().getPaymentId()
                        ,paymentEntityOptional.get().getCheckoutUrl(), PaymentsConstants.CHECKOUT_STATUS_SUCCESS);

            }

        }


        Stripe.apiKey = stripeProperties.getApiKey();

        long amountInPaise = bookingInfoDto.getPrice()
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();

        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
                .builder().setName(PaymentsConstants.PAYMENT_DISPLAY).build();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData
                .builder()
                .setCurrency(PaymentsConstants.PAYMENT_CURRENCY)
                .setProductData(productData)
                .setUnitAmount(amountInPaise).build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem
                .builder().setPriceData(priceData)
                .setQuantity(1L).build();

        SessionCreateParams params= SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(stripeProperties.getSuccessUrl())
                .setCancelUrl(stripeProperties.getCancelUrl())
                .addLineItem(lineItem)
                .build();

        PaymentEntity paymentEntity = PaymentsMapper.mapBookingsInfoDtoToPaymentEntity(bookingInfoDto);

        paymentEntity.setPaymentId(paymentId);

        Session session;
        try {
            session = Session.create(params);
        } catch (StripeException e){

            paymentEntity.setPaymentStatus(PaymentsConstants.PAYMENT_CHECKOUT_FAILED);

            paymentsRepository.save(paymentEntity);

            return new PaymentResponseDto(bookingInfoDto.getBookingId(),
                "","","", PaymentsConstants.CHECKOUT_STATUS_FAILURE);

        }

        paymentEntity.setStripeCheckoutSessionId(session.getId());
        paymentEntity.setCheckoutUrl(session.getUrl());
        paymentEntity.setStripePaymentIntent(session.getPaymentIntent());

        paymentsRepository.save(paymentEntity);

        return new PaymentResponseDto(bookingInfoDto.getBookingId(),
                session.getId(),paymentId,session.getUrl(),PaymentsConstants.CHECKOUT_STATUS_SUCCESS);
    }
}
