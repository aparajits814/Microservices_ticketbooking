package com.booking.show.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowPricingId implements Serializable {

    private String showId;

    private String seatType;



}
