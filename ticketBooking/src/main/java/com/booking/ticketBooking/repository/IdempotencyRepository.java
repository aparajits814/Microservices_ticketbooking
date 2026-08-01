package com.booking.ticketBooking.repository;


import com.booking.ticketBooking.entity.IdempotencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdempotencyRepository extends JpaRepository<IdempotencyEntity, String> {
}
