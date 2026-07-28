package com.booking.show.repository;

import com.booking.show.entity.ShowSeatEntity;
import com.booking.show.entity.ShowSeatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeatEntity, ShowSeatId> {

    List<ShowSeatEntity> findByShowId(String showId);

}
