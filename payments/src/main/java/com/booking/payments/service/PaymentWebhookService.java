package com.booking.payments.service;

import com.stripe.exception.SignatureVerificationException;

public interface PaymentWebhookService {

    void processWebHook(String payload,String stripeSignature) throws SignatureVerificationException;

}
