package com.booking.show.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatsLockDto {

    private String bookingId;

    private String showId;

    private List<String> seatIds;

}
