package com.booking.ticketBooking.service;

import com.booking.ticketBooking.dto.SeatLockResponseDto;
import com.booking.ticketBooking.dto.SeatsLockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("show")
public interface ShowFeignClient {

    @PostMapping("/api/lockSeats")
    public ResponseEntity<SeatLockResponseDto> lockSeats(@RequestBody SeatsLockDto seatsLockDto);

}
