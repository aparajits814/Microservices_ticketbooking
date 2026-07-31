package com.booking.show.repository;

import com.booking.show.entity.ShowSeatOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowSeatOutboxRepository extends JpaRepository<ShowSeatOutboxEntity,String> {

    boolean existsByBookingIdAndPaymentId(String bookingId,String paymentId);

    List<ShowSeatOutboxEntity> findByProcessed(boolean processed);

    Optional<ShowSeatOutboxEntity> findByPaymentIdAndBookingId(String paymentId, String bookingId);

}
