package com.booking.payments.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name="payment_outbox")
@Getter
@Setter
public class PaymentPollerEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;

    private String bookingId;

    private String paymentId;

    private String eventType;

    private String topic;

    private String payload;

    private Boolean processed;

    private LocalDateTime createdAt;

}
