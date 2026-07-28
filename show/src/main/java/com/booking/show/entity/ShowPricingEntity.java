package com.booking.show.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Table(name="show_pricing")
@Entity
@Getter
@Setter
@IdClass(ShowPricingId.class)
public class ShowPricingEntity {

    @Id
    private String showId;

    @Id
    private String seatType;

    private BigDecimal price;

}
