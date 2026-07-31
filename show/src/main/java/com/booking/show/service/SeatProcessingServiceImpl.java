package com.booking.show.service;

import com.booking.show.constants.ShowConstants;
import com.booking.show.dto.ProcessingDto;
import com.booking.show.entity.ShowSeatEntity;
import com.booking.show.repository.ShowSeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SeatProcessingServiceImpl implements SeatProcessingService{

    private ShowSeatRepository showSeatRepository;

    private SeatOutboxService seatOutboxService;

    @Override
    @KafkaListener(topics = ShowConstants.BOOKING_SUCCESS_TOPIC)
    @Transactional
    public void processBookingSuccessEvent(ProcessingDto processingDto) {

        List<ShowSeatEntity> showSeatEntityList = showSeatRepository.findByLockedByBookingId(processingDto.getBookingId());
        if(showSeatEntityList.isEmpty()){
            try {
                seatOutboxService.publishSeatConfirmFailedEvent(processingDto);
            }catch(Exception e){
                return;
            }
            return;
        }

        for(ShowSeatEntity showSeatEntity : showSeatEntityList){
            showSeatEntity.setSeatStatus(ShowConstants.SEAT_STATUS_BOOKED);
        }

        try {
            showSeatRepository.flush();
        }catch(DataIntegrityViolationException e){
            try {
                seatOutboxService.publishSeatConfirmFailedEvent(processingDto);
            }catch (Exception ex){
                return;
            }
            return;
        }

        try {
            seatOutboxService.publishSeatConfirmedSuccessEvent(processingDto);
        }catch (Exception ignored){

        }


    }

    @Override
    @KafkaListener(topics = ShowConstants.BOOKING_FAILED_TOPIC)
    @Transactional
    public void processBookingFailedEvent(ProcessingDto processingDto) {

        List<ShowSeatEntity> showSeatEntityList = showSeatRepository.findByLockedByBookingId(processingDto.getBookingId());

        for(ShowSeatEntity showSeatEntity : showSeatEntityList){
            if(!showSeatEntity.getSeatStatus().equalsIgnoreCase(ShowConstants.SEAT_STATUS_AVALIABLE)){
                showSeatEntity.setSeatStatus(ShowConstants.SEAT_STATUS_AVALIABLE);
            }
        }

        showSeatRepository.saveAll(showSeatEntityList);

    }
}
