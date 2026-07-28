package com.booking.payments.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="payments")
@Getter
@Setter
public class PaymentEntity {

    @Id
    private String paymentId;

    private String bookingId;

    private BigDecimal paymentAmount;

    private String currency;

    private String paymentStatus;

    private String stripeCheckoutSessionId;

    private String CheckoutUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Version
    private Long Version;

}
