package com.booking.ticketBooking.service;

import com.booking.ticketBooking.constants.BookingConstants;
import com.booking.ticketBooking.dto.ProcessingDto;
import com.booking.ticketBooking.entity.BookingEntity;
import com.booking.ticketBooking.entity.IdempotencyEntity;
import com.booking.ticketBooking.repository.BookingRepository;
import com.booking.ticketBooking.repository.IdempotencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookingProcessingServiceImpl implements BookingProcessingService {

    private BookingRepository bookingRepository;

    private BookingOutboxService bookingOutboxService;

    private IdempotencyRepository idempotencyRepository;

    @Override
    @Transactional
    public void processPaymentSuccessEvent(ProcessingDto processingDto) {

        saveProcessedEvent(processingDto);

        processingDto.setEventType(BookingConstants.BOOKING_SUCCESS_TOPIC);
        bookingOutboxService.publishEvent(processingDto, BookingConstants.BOOKING_SUCCESS_TOPIC, BookingConstants.BOOKING_SUCCESS_TOPIC);

        Optional<BookingEntity> bookingEntityOptional = bookingRepository.findById(processingDto.getBookingId());

        if(bookingEntityOptional.isEmpty()) {
            processingDto.setEventType(BookingConstants.BOOKING_FAILED_TOPIC);
            bookingOutboxService.publishEvent(processingDto, BookingConstants.BOOKING_FAILED_TOPIC, BookingConstants.BOOKING_FAILED_TOPIC);
            return;
        }

        BookingEntity bookingEntity = bookingEntityOptional.get();
        System.out.println("Booking status="+bookingEntity.getBookingStatus());

        if(!bookingEntity.getBookingStatus().equalsIgnoreCase(BookingConstants.BOOKING_STATUS_PENDING)){
            throw new IllegalStateException();
        }

        if(bookingEntity.getPaymentId()!=null){
            return;
        }

        bookingEntity.setUpdatedAt(LocalDateTime.now());
        bookingEntity.setPaymentId(processingDto.getPaymentId());
        bookingEntity.setBookingStatus(BookingConstants.BOOKING_STATUS_PENDING_CONFIRMATION);

        bookingRepository.save(bookingEntity);


    }

    @Override
    @Transactional
    public void processPaymentExpiredEvent(ProcessingDto processingDto) {

        saveProcessedEvent(processingDto);

        Optional<BookingEntity> bookingEntityOptional = bookingRepository.findById(processingDto.getBookingId());

        if(bookingEntityOptional.isEmpty()) {
            processingDto.setEventType(BookingConstants.SEAT_RELEASE_TOPIC);
            bookingOutboxService.publishEvent(processingDto, BookingConstants.SEAT_RELEASE_TOPIC, BookingConstants.SEAT_RELEASE_TOPIC);
            return;
        }

        BookingEntity bookingEntity = bookingEntityOptional.get();

        if(!bookingEntity.getBookingStatus().equalsIgnoreCase(BookingConstants.BOOKING_STATUS_PENDING)){
            throw new IllegalStateException();
        }

        if(bookingEntity.getPaymentId()!=null){
            return;
        }

        bookingEntity.setUpdatedAt(LocalDateTime.now());
        bookingEntity.setPaymentId(processingDto.getPaymentId());
        bookingEntity.setBookingStatus(BookingConstants.BOOKING_STATUS_FAILED);

        bookingRepository.save(bookingEntity);
        processingDto.setEventType(BookingConstants.SEAT_RELEASE_TOPIC);
        bookingOutboxService.publishEvent(processingDto, BookingConstants.SEAT_RELEASE_TOPIC, BookingConstants.SEAT_RELEASE_TOPIC);

    }

    @Override
    @Transactional
    public void processSeatConfirmedEvent(ProcessingDto processingDto) {

        saveProcessedEvent(processingDto);

        Optional<BookingEntity> bookingEntityOptional = bookingRepository.findById(processingDto.getBookingId());

        BookingEntity bookingEntity = bookingEntityOptional.get();
        bookingEntity.setBookingStatus(BookingConstants.BOOKING_STATUS_SUCCESS);
        bookingEntity.setUpdatedAt(LocalDateTime.now());

        bookingRepository.save(bookingEntity);

        //publish Notification event
    }

    @Override
    @Transactional
    public void processSeatFailedEvent(ProcessingDto processingDto) {

        saveProcessedEvent(processingDto);

        Optional<BookingEntity> bookingEntityOptional = bookingRepository.findById(processingDto.getBookingId());

        BookingEntity bookingEntity = bookingEntityOptional.get();

        bookingEntity.setBookingStatus(BookingConstants.BOOKING_STATUS_FAILED);
        bookingEntity.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(bookingEntity);
        processingDto.setEventType(BookingConstants.BOOKING_FAILED_TOPIC);
        bookingOutboxService.publishEvent(processingDto, BookingConstants.BOOKING_FAILED_TOPIC, BookingConstants.BOOKING_FAILED_TOPIC);
    }

    private void saveProcessedEvent(ProcessingDto processingDto){
        IdempotencyEntity idempotencyEntity = new IdempotencyEntity();
        idempotencyEntity.setBookingId(processingDto.getBookingId());
        idempotencyEntity.setPaymentId(processingDto.getPaymentId());
        idempotencyEntity.setEventType(processingDto.getEventType());

        idempotencyRepository.save(idempotencyEntity);
    }
}
