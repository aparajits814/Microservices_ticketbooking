package com.booking.show.repository;

import com.booking.show.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity,String> {

    List<SeatEntity> findByScreenId(String screenId);

}
