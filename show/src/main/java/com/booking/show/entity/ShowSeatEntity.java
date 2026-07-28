package com.booking.show.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "show_seats")
@IdClass(ShowSeatId.class)
public class ShowSeatEntity {

    @Id
    private String showId;

    @Id
    private String seatId;

    private String seatStatus;

    private String lockedByBookingId;

    private LocalDateTime lockExpiry;

    @Version
    private Integer version;

}
