package com.booking.payments.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stripe")
@Getter
@Setter
public class StripeProperties {

    private String apiKey;

    private String successUrl;

    private String cancelUrl;

    private String webhookKey;

}
