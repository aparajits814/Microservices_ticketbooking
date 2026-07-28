package com.booking.payments.service;

import com.booking.payments.dto.PaymentCheckoutDto;
import com.booking.payments.dto.PaymentResponseDto;

public interface PaymentCheckoutService {

    PaymentResponseDto createPaymentCheckout(PaymentCheckoutDto paymentCheckoutDto);

}
