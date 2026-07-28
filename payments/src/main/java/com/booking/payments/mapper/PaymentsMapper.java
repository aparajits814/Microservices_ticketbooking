package com.booking.payments.mapper;

import com.booking.payments.constants.PaymentsConstants;
import com.booking.payments.dto.BookingInfoDto;
import com.booking.payments.entity.PaymentEntity;

import java.time.LocalDateTime;

public class PaymentsMapper {

    private PaymentsMapper(){

    }

    public static PaymentEntity mapBookingsInfoDtoToPaymentEntity(BookingInfoDto bookingInfoDto){

        PaymentEntity paymentEntity = new PaymentEntity();

        paymentEntity.setBookingId(bookingInfoDto.getBookingId());
        paymentEntity.setPaymentAmount(bookingInfoDto.getPrice());
        paymentEntity.setPaymentStatus(PaymentsConstants.PAYMENT_INITIATED);
        paymentEntity.setCurrency(PaymentsConstants.PAYMENT_CURRENCY);
        paymentEntity.setCreatedAt(LocalDateTime.now());

        return paymentEntity;

    }
}
