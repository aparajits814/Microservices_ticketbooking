package com.booking.movie.repository;

import com.booking.movie.entity.LanguagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguagesRepository extends JpaRepository<LanguagesEntity, String> {

    Optional<LanguagesEntity> findByLanguage(String language);

}
