package com.booking.show.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name="idempotency_check")
@Getter
@Setter
public class IdempotencyEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;

    private String bookingId;

    private String paymentId;

    private String eventType;


}
