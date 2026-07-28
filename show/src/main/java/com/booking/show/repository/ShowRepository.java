package com.booking.show.repository;

import com.booking.show.entity.ShowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<ShowEntity,String> {

    boolean existsByScreenIdAndShowStartTimeLessThanAndShowEndTimeGreaterThan(
            String screenId,
            LocalDateTime showStartTime,
            LocalDateTime showEndTime
    );

    List<ShowEntity> findByMovieId(String movieId);

}
