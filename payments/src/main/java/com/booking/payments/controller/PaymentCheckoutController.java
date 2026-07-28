package com.booking.payments.controller;

import com.booking.payments.dto.PaymentCheckoutDto;
import com.booking.payments.dto.PaymentResponseDto;
import com.booking.payments.service.PaymentCheckoutService;
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
public class PaymentCheckoutController {

    private PaymentCheckoutService paymentCheckoutService;

    @PostMapping("/createCheckout")
    public ResponseEntity<PaymentResponseDto> createCheckout(@RequestBody PaymentCheckoutDto paymentCheckoutDto){

        PaymentResponseDto paymentCheckoutResponseDto = paymentCheckoutService.createPaymentCheckout(paymentCheckoutDto);

        return new ResponseEntity<>(paymentCheckoutResponseDto, HttpStatus.OK);

    }

}
