package com.booking.show.repository;

import com.booking.show.entity.TheatreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheatreRepository extends JpaRepository<TheatreEntity,String> {

    List<TheatreEntity> findByLocation(String location);

}
