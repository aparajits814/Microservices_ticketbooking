package com.booking.show.service;

import com.booking.show.constants.ShowConstants;
import com.booking.show.dto.ProcessingDto;
import com.booking.show.entity.ShowSeatOutboxEntity;
import com.booking.show.repository.ShowSeatOutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SeatOutboxServiceImpl implements SeatOutboxService{

    private ShowSeatOutboxRepository showSeatOutboxRepository;

    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public void publishSeatConfirmedSuccessEvent(ProcessingDto processingDto) throws JsonProcessingException {

        boolean exists = showSeatOutboxRepository.
                existsByBookingIdAndPaymentId(processingDto.getBookingId(), processingDto.getPaymentId());

        if(exists){
            return;
        }

        String responsePayload = objectMapper.writeValueAsString(processingDto);

        ShowSeatOutboxEntity showSeatOutboxEntity = new ShowSeatOutboxEntity();
        showSeatOutboxEntity.setBookingId(processingDto.getBookingId());
        showSeatOutboxEntity.setPaymentId(processingDto.getPaymentId());
        showSeatOutboxEntity.setTopic(ShowConstants.SEAT_CONFIRMED_TOPIC);
        showSeatOutboxEntity.setEventType("");
        showSeatOutboxEntity.setProcessed(false);
        showSeatOutboxEntity.setCreatedAt(LocalDateTime.now());
        showSeatOutboxEntity.setPayload(responsePayload);
        showSeatOutboxRepository.save(showSeatOutboxEntity);

    }

    @Override
    public void publishSeatConfirmFailedEvent(ProcessingDto processingDto) throws JsonProcessingException {

        boolean exists = showSeatOutboxRepository.
                existsByBookingIdAndPaymentId(processingDto.getBookingId(), processingDto.getPaymentId());

        if(exists){
            return;
        }

        String responsePayload = objectMapper.writeValueAsString(processingDto);

        ShowSeatOutboxEntity showSeatOutboxEntity = new ShowSeatOutboxEntity();
        showSeatOutboxEntity.setPaymentId(processingDto.getPaymentId());
        showSeatOutboxEntity.setBookingId(processingDto.getBookingId());
        showSeatOutboxEntity.setTopic(ShowConstants.SEAT_FAILED_TOPIC);
        showSeatOutboxEntity.setEventType("");
        showSeatOutboxEntity.setProcessed(false);
        showSeatOutboxEntity.setCreatedAt(LocalDateTime.now());
        showSeatOutboxEntity.setPayload(responsePayload);
        showSeatOutboxRepository.save(showSeatOutboxEntity);

    }
}
