package com.booking.show.service;

import com.booking.show.entity.ShowSeatEntity;
import com.booking.show.repository.ShowSeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class SeatUnlockPoller {

    private ShowSeatRepository showSeatRepository;

    private ShowLockService showLockService;

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.MINUTES)
    public void unlockExpiredSeats(){

        List<ShowSeatEntity> expiredSeatsList = showSeatRepository.findByLockExpiryBefore(LocalDateTime.now());

        for(ShowSeatEntity showSeatEntity : expiredSeatsList){

            showLockService.unlockSeat(showSeatEntity.getSeatId(), showSeatEntity.getShowId(), showSeatEntity.getVersion());

        }

    }

}
