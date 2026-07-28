package com.booking.ticketBooking.repository;

import com.booking.ticketBooking.entity.BookingSeatEntity;
import com.booking.ticketBooking.entity.BookingSeatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeatEntity, BookingSeatId> {

}
