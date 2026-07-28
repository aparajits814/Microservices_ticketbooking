package com.booking.payments.service;

import com.booking.payments.dto.BookingInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("ticketbooking")
public interface BookingFeignClient {

    @GetMapping("/api/findBooking")
    public ResponseEntity<BookingInfoDto> findBookingById(@RequestParam String bookingId);

}
