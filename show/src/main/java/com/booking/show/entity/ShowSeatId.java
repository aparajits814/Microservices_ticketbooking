package com.booking.show.entity;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShowSeatId implements Serializable {

    private String seatId;

    private String showId;
}
