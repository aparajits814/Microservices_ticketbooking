package com.booking.show.controller;

import com.booking.show.dto.SeatLockResponseDto;
import com.booking.show.dto.SeatsLockDto;
import com.booking.show.service.ShowLockService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class ShowLockController {

    private ShowLockService showLockService;

    @PostMapping("/lockSeats")
    public ResponseEntity<SeatLockResponseDto> lockSeats(@RequestBody SeatsLockDto seatsLockDto){

        SeatLockResponseDto seatLockResponseDto = showLockService.lockSeats(seatsLockDto);

        return new ResponseEntity<>(seatLockResponseDto, HttpStatus.OK);

    }

}
