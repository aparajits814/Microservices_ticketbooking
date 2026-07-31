package com.booking.ticketBooking.repository;

import com.booking.ticketBooking.entity.BookingSeatOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingSeatOutboxRepository extends JpaRepository<BookingSeatOutboxEntity,String> {

    boolean existsByBookingIdAndPaymentId(String bookingId,String paymentId);

    List<BookingSeatOutboxEntity> findByProcessed(boolean processed);

    Optional<BookingSeatOutboxEntity> findByPaymentIdAndBookingId(String paymentId, String bookingId);

}
