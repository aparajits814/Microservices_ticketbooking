package com.booking.movie.repository;

import com.booking.movie.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, String> {

    Optional<GenreEntity> findByGenreType(String genreType);

}
