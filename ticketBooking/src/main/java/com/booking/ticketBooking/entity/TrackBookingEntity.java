package com.booking.ticketBooking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="track_book")
@Getter
@Setter
@IdClass(TrackBookingId.class)
public class TrackBookingEntity {

    @Id
    private String paymentId;

    @Id
    private String bookingId;

}
