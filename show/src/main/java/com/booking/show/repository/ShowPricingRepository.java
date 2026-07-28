package com.booking.show.repository;

import com.booking.show.entity.ShowPricingEntity;
import com.booking.show.entity.ShowPricingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowPricingRepository extends JpaRepository<ShowPricingEntity, ShowPricingId> {

    List<ShowPricingEntity> findByShowId(String showId);

}
