package com.booking.ticketBooking.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="booking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookingEntity {

    @Id
    private String bookingId;

    private String showId;

    private String userId;

    private String bookingStatus;

    private String paymentId;

    private BigDecimal paymentAmount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
