package com.booking.payments.controller;

import com.booking.payments.service.PaymentWebhookService;
import com.stripe.exception.SignatureVerificationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class PaymentsWebhookController {

    private PaymentWebhookService webhookService;

    @PostMapping("/webhook")
    public ResponseEntity<Void> createWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String stripeSignature
    ) throws SignatureVerificationException {
        webhookService.processWebHook(payload,stripeSignature);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
