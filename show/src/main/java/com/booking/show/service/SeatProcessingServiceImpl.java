package com.booking.show.service;

import com.booking.show.constants.ShowConstants;
import com.booking.show.dto.ProcessingDto;
import com.booking.show.entity.IdempotencyEntity;
import com.booking.show.entity.ShowSeatEntity;
import com.booking.show.repository.IdempotencyRepository;
import com.booking.show.repository.ShowSeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SeatProcessingServiceImpl implements SeatProcessingService{

    private ShowSeatRepository showSeatRepository;

    private SeatOutboxService seatOutboxService;

    private IdempotencyRepository idempotencyRepository;

    @Override
    @Transactional
    public void processBookingSuccessEvent(ProcessingDto processingDto) {

        saveProcessedEvent(processingDto);

        List<ShowSeatEntity> showSeatEntityList = showSeatRepository.findByLockedByBookingId(processingDto.getBookingId());
        if(showSeatEntityList.isEmpty()){
            processingDto.setEventType(ShowConstants.SEAT_FAILED_TOPIC);
            seatOutboxService.publishEvent(processingDto,ShowConstants.SEAT_FAILED_TOPIC, ShowConstants.SEAT_FAILED_TOPIC);
            return;
        }

        for(ShowSeatEntity showSeatEntity : showSeatEntityList){
            showSeatEntity.setSeatStatus(ShowConstants.SEAT_STATUS_BOOKED);
        }

        try {
            showSeatRepository.flush();
        }catch(DataIntegrityViolationException e){
            processingDto.setEventType(ShowConstants.SEAT_FAILED_TOPIC);
            seatOutboxService.publishEvent(processingDto,ShowConstants.SEAT_FAILED_TOPIC, ShowConstants.SEAT_FAILED_TOPIC);
            return;
        }

        processingDto.setEventType(ShowConstants.SEAT_CONFIRMED_TOPIC);
        seatOutboxService.publishEvent(processingDto, ShowConstants.SEAT_CONFIRMED_TOPIC, ShowConstants.SEAT_CONFIRMED_TOPIC);


    }

    @Override
    @Transactional
    public void processBookingFailedEvent(ProcessingDto processingDto) {

        saveProcessedEvent(processingDto);

        List<ShowSeatEntity> showSeatEntityList = showSeatRepository.findByLockedByBookingId(processingDto.getBookingId());

        for(ShowSeatEntity showSeatEntity : showSeatEntityList){
            if(!showSeatEntity.getSeatStatus().equalsIgnoreCase(ShowConstants.SEAT_STATUS_AVAILABLE)){
                showSeatEntity.setSeatStatus(ShowConstants.SEAT_STATUS_AVAILABLE);
            }
        }

        showSeatRepository.saveAll(showSeatEntityList);

    }

    private void saveProcessedEvent(ProcessingDto processingDto){
        IdempotencyEntity idempotencyEntity = new IdempotencyEntity();
        idempotencyEntity.setBookingId(processingDto.getBookingId());
        idempotencyEntity.setPaymentId(processingDto.getPaymentId());
        idempotencyEntity.setEventType(processingDto.getEventType());

        idempotencyRepository.save(idempotencyEntity);
    }
}
