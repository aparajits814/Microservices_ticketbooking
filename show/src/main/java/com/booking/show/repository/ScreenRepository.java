package com.booking.show.repository;

import com.booking.show.entity.ScreenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScreenRepository extends JpaRepository<ScreenEntity,String> {

    List<ScreenEntity> findByTheatreId(String theatreId);

}
