package com.booking.movie.repository;

import com.booking.movie.entity.MoviesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoviesRepository extends JpaRepository<MoviesEntity, String> {

    Optional<MoviesEntity> findByMovieName(String movieName);

}
