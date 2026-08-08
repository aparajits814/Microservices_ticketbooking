package com.booking.payments.service;

import com.booking.payments.configuration.StripeProperties;
import com.booking.payments.constants.PaymentsConstants;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentWebhookServiceImpl implements PaymentWebhookService{

    private final StripeProperties stripeProperties;

    private PaymentService paymentService;

    @Override
    public void processWebHook(String payload, String stripeSignature) throws SignatureVerificationException {
        Event event = Webhook.constructEvent(
                payload,
                stripeSignature,
                stripeProperties.getWebhookKey()
        );

        switch (event.getType()){

            case PaymentsConstants.CHECKOUT_COMPLETED ->
                handleCheckoutCompleted(event);
            case PaymentsConstants.CHECKOUT_EXPIRED ->
                handleCheckoutExpired(event);

        }

        log.info("event:{}", event.getType());
    }

    private void handleCheckoutCompleted(Event event){

        Session session = extractCheckoutSession(event);
        paymentService.paymentSuccess(session.getId(), session.getPaymentStatus(), session.getPaymentIntent());

    }

    private void handleCheckoutExpired(Event event){

        Session session = extractCheckoutSession(event);
        paymentService.paymentExpired(session.getId());

    }

    private Session extractCheckoutSession(Event event) {

        StripeObject stripeObject = event
                .getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Unable to deserialize Stripe event: "
                                        + event.getId()
                        )
                );

        if (!(stripeObject instanceof Session session)) {
            throw new IllegalArgumentException(
                    "Expected Checkout Session for event: "
                            + event.getType()
            );
        }

        return session;
    }
}
