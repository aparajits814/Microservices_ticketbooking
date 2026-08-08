package com.booking.show.service;

import com.booking.show.constants.ShowConstants;
import com.booking.show.dto.SeatLockResponseDto;
import com.booking.show.dto.SeatsLockDto;
import com.booking.show.dto.ShowInfoDto;
import com.booking.show.entity.ShowSeatEntity;
import com.booking.show.entity.ShowSeatId;
import com.booking.show.exceptions.IllegalShowException;
import com.booking.show.exceptions.SeatUnavailableException;
import com.booking.show.exceptions.ShowInactiveException;
import com.booking.show.repository.ShowSeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShowLockServiceImpl implements ShowLockService{

    private ShowSeatRepository showSeatRepository;

    private ShowService showService;

    @Override
    @Transactional
    public SeatLockResponseDto lockSeats(SeatsLockDto seatsLockDto) {

        Set<String> seatIdRequestedList = new HashSet<>(seatsLockDto.getSeatIds());
        System.out.println("Printing seats");
        for(String s: seatIdRequestedList){
            System.out.println(s+" ");
        }

        List<ShowSeatEntity> showSeatEntityList = showSeatRepository.findByShowId(seatsLockDto.getShowId());
        Set<String> seatIdForShow = showSeatEntityList.stream()
                .map(ShowSeatEntity::getSeatId).collect(Collectors.toSet());
        List<ShowSeatEntity> showSeatEntityListRequested = showSeatEntityList.stream().filter(
                showSeatEntity -> seatIdRequestedList.contains(showSeatEntity.getSeatId())
        ).toList();

        ShowInfoDto showInfoDto = showService.findShowByShowId(seatsLockDto.getShowId());
        if(!ShowConstants.SHOW_STATUS_ACTIVE.equalsIgnoreCase(showInfoDto.getShowStatus())){
            throw new ShowInactiveException(ShowConstants.ILLEGAL_SHOW_EXCEPTION);
        }
        System.out.println("After Show status");

        if(LocalDateTime.now().isAfter(showInfoDto.getShowStartTime())){
            throw new IllegalShowException(ShowConstants.ILLEGAL_SHOW_EXCEPTION);
        }
        System.out.println("After date and time check");


        for(String seatId : seatIdRequestedList){
            if(!seatIdForShow.contains(seatId)){
                throw new IllegalShowException(ShowConstants.ILLEGAL_SHOW_EXCEPTION);
            }
        }
        System.out.println("After Seat Check");

        for(ShowSeatEntity showSeatEntity : showSeatEntityListRequested){
            boolean available = showSeatEntity.getSeatStatus().equals(ShowConstants.SEAT_STATUS_AVAILABLE);

            boolean expiredLock =
                    ShowConstants.SEAT_STATUS_LOCKED.equals(showSeatEntity.getSeatStatus()) && showSeatEntity.getLockExpiry() != null
                            && !showSeatEntity.getLockExpiry().isAfter(LocalDateTime.now());

            if(!available && !expiredLock){

                throw new SeatUnavailableException(ShowConstants.SEAT_UNAVAILABLE_EXCEPTION);

            }

            showSeatEntity.setLockExpiry(LocalDateTime.now().plusMinutes(2));
            showSeatEntity.setLockedByBookingId(seatsLockDto.getBookingId());
            showSeatEntity.setSeatStatus(ShowConstants.SEAT_STATUS_LOCKED);
        }
        System.out.println("After Upadting");

        try {
            showSeatRepository.flush();
        }catch (ObjectOptimisticLockingFailureException e){
            throw new SeatUnavailableException(ShowConstants.SEAT_UNAVAILABLE_EXCEPTION);
        }
        System.out.println("After flush");

        return new SeatLockResponseDto(ShowConstants.SEAT_STATUS_LOCKED,showInfoDto);
    }

    @Override
    @Transactional
    public void unlockSeat(String seatId, String showId, Integer expectedVersion) {

        Optional<ShowSeatEntity> showSeatEntityOptional = showSeatRepository.findById(new ShowSeatId(seatId, showId));

        if(showSeatEntityOptional.isEmpty()){
            return;
        }

        ShowSeatEntity showSeatEntity = showSeatEntityOptional.get();

        if (!Objects.equals(showSeatEntity.getVersion(), expectedVersion)) {
            return;
        }

        if(!ShowConstants.SEAT_STATUS_LOCKED.equalsIgnoreCase(showSeatEntity.getSeatStatus())){
            return;
        }

        showSeatEntity.setLockExpiry(null);
        showSeatEntity.setLockedByBookingId(null);
        showSeatEntity.setSeatStatus(ShowConstants.SEAT_STATUS_AVAILABLE);

        showSeatRepository.flush();

    }
}
