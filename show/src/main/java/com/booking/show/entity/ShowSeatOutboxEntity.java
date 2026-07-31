package com.booking.show.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name="seat_outbox")
@Getter
@Setter
public class ShowSeatOutboxEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;

    private String bookingId;

    private String paymentId;

    private String eventType;

    private String payload;

    private boolean processed;

    private LocalDateTime createdAt;

    private String topic;

}
