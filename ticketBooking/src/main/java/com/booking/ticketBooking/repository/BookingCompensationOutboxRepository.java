package com.booking.ticketBooking.repository;

import com.booking.ticketBooking.entity.BookingCompensationOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingCompensationOutboxRepository extends JpaRepository<BookingCompensationOutboxEntity,String> {

    boolean existsByBookingIdAndPaymentId(String bookingId,String paymentId);

    List<BookingCompensationOutboxEntity> findByProcessed(boolean processed);

    Optional<BookingCompensationOutboxEntity> findByPaymentIdAndBookingId(String paymentId, String bookingId);

}
