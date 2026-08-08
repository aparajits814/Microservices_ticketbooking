package com.booking.show.repository;

import com.booking.show.entity.ShowSeatEntity;
import com.booking.show.entity.ShowSeatId;
import org.springframework.boot.actuate.endpoint.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeatEntity, ShowSeatId> {

    List<ShowSeatEntity> findByShowId(String showId);

    List<ShowSeatEntity> findByLockedByBookingId(String bookingId);

    List<ShowSeatEntity> findByLockExpiryBefore(LocalDateTime date);

}
