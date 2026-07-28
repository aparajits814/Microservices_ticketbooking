package com.booking.show.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowDto {

    private String showId;

    private String movieId;

    private String screenId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    List<SeatPricingDto> seatPricing;

}
