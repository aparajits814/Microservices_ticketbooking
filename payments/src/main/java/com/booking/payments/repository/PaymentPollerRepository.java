package com.booking.payments.repository;

import com.booking.payments.entity.PaymentPollerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentPollerRepository extends JpaRepository<PaymentPollerEntity,String> {

    boolean existsByPaymentIdAndBookingId(String paymentId,String bookingId);

    List<PaymentPollerEntity> findByProcessed(boolean processed);

    Optional<PaymentPollerEntity> findByPaymentIdAndBookingId(String paymentId,String bookingId);


}
