package com.booking.payments.repository;

import com.booking.payments.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<PaymentEntity,String> {

    Optional<PaymentEntity> findByBookingId(String bookingId);

    Optional<PaymentEntity> findByStripeCheckoutSessionId(String sessionId);
}
