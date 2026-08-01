package com.booking.show.service;

import com.booking.show.constants.ShowConstants;
import com.booking.show.dto.ProcessingDto;
import com.booking.show.repository.ShowSeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class ShowListener {

    private SeatProcessingService seatProcessingService;

    @KafkaListener(topics = ShowConstants.BOOKING_SUCCESS_TOPIC)
    public void processBookingSuccessEvent(ProcessingDto processingDto) {

        seatProcessingService.processBookingSuccessEvent(processingDto);

    }

    @KafkaListener(topics = ShowConstants.SEAT_RELEASE_TPOIC)
    public void processBookingFailedEvent(ProcessingDto processingDto) {

        seatProcessingService.processBookingFailedEvent(processingDto);

    }
}
