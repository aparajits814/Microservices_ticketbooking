package com.booking.ticketBooking.repository;

import com.booking.ticketBooking.entity.TrackBookingEntity;
import com.booking.ticketBooking.entity.TrackBookingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackBookingRepository extends JpaRepository<TrackBookingEntity, TrackBookingId> {

}
