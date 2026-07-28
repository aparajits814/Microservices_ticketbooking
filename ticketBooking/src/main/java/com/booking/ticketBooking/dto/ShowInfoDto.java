package com.booking.ticketBooking.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShowInfoDto {

    private String ShowId;

    private String movieId;

    private List<String> seatIds;

    private LocalDateTime showStartTime;

    private LocalDateTime showEndTime;

    private List<SeatPricingDto> seatsPricing;

    private String showStatus;

}
