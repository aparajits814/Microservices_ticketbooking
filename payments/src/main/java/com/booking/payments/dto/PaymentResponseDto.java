package com.booking.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {

    private String bookingId;

    private String sessionId;

    private String paymentId;

    private String checkoutUrl;

    private String checkoutStatus;

}
