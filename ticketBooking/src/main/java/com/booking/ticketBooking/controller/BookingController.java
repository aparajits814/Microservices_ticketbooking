package com.booking.ticketBooking.controller;

import com.booking.ticketBooking.dto.BookingDto;
import com.booking.ticketBooking.dto.BookingInfoDto;
import com.booking.ticketBooking.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
@AllArgsConstructor
public class BookingController {

    private BookingService bookingService;

    @PostMapping("/createBooking")
    public ResponseEntity<BookingDto> createBookingForShow(@RequestBody BookingDto bookingDto){

        BookingDto bookingDtoUpdated = bookingService.createBooking(bookingDto);

        return new ResponseEntity<>(bookingDtoUpdated, HttpStatus.OK);

    }

    @GetMapping("/findBooking")
    public ResponseEntity<BookingInfoDto> findBookingById(@RequestParam String bookingId){

        BookingInfoDto bookingInfoDto = bookingService.findBooking(bookingId);

        return new ResponseEntity<>(bookingInfoDto,HttpStatus.OK);

    }

}
