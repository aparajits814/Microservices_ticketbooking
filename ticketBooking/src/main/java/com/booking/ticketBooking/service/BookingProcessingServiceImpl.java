package com.booking.ticketBooking.service;

import com.booking.ticketBooking.constants.BookingConstants;
import com.booking.ticketBooking.dto.ProcessingDto;
import com.booking.ticketBooking.entity.BookingEntity;
import com.booking.ticketBooking.repository.BookingRepository;
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

    @Override
    @KafkaListener(topics = BookingConstants.PAYMENT_SUCCESS_TOPIC)
    @Transactional
    public void processPaymentSuccessEvent(ProcessingDto processingDto) {

        try {
            bookingOutboxService.publishBookingSuccessEvent(processingDto, BookingConstants.BOOKING_PENDING_CONFIRMATION_EVENT);
        }catch(Exception e){
            return;
        }

        Optional<BookingEntity> bookingEntityOptional = bookingRepository.findById(processingDto.getBookingId());

        if(bookingEntityOptional.isEmpty()) {
            try {
                bookingOutboxService.publishBookingFailedEvent(processingDto, BookingConstants.BOOKING_NOT_EXISTS_EVENT);
            } catch (Exception e) {

                return;

            }

            return;
        }

        BookingEntity bookingEntity = bookingEntityOptional.get();
        System.out.println("Booking status="+bookingEntity.getBookingStatus());

        if(!bookingEntity.getBookingStatus().equalsIgnoreCase(BookingConstants.BOOKING_STATUS_PENDING)){
            return;
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
    @KafkaListener(topics = BookingConstants.PAYMENT_EXPIRE_TOPIC)
    @Transactional
    public void processPaymentExpiredEvent(ProcessingDto processingDto) {

        Optional<BookingEntity> bookingEntityOptional = bookingRepository.findById(processingDto.getBookingId());

        if(bookingEntityOptional.isEmpty()) {

            try {

                bookingOutboxService.publishBookingFailedEvent(processingDto, BookingConstants.BOOKING_NOT_EXISTS_EVENT);

            }catch (Exception e){

            }

            return;
        }

        BookingEntity bookingEntity = bookingEntityOptional.get();

        if(!bookingEntity.getBookingStatus().equalsIgnoreCase(BookingConstants.BOOKING_STATUS_PENDING)){
            return;
        }

        if(bookingEntity.getPaymentId()!=null){
            return;
        }

        bookingEntity.setUpdatedAt(LocalDateTime.now());
        bookingEntity.setPaymentId(processingDto.getPaymentId());
        bookingEntity.setBookingStatus(BookingConstants.BOOKING_STATUS_PENDING_CONFIRMATION);

        bookingRepository.save(bookingEntity);
        try {
            bookingOutboxService.publishBookingSuccessEvent(processingDto, BookingConstants.BOOKING_PENDING_CONFIRMATION_EVENT);
        }catch(Exception e){

        }

    }

    @Override
    @Transactional
    @KafkaListener(topics = BookingConstants.SEAT_CONFIRMED_TOPIC)
    public void processSeatConfirmedEvent(ProcessingDto processingDto) {

        Optional<BookingEntity> bookingEntityOptional = bookingRepository.findById(processingDto.getBookingId());

        BookingEntity bookingEntity = bookingEntityOptional.get();
        bookingEntity.setBookingStatus(BookingConstants.BOOKING_STATUS_SUCCESS);
        bookingEntity.setUpdatedAt(LocalDateTime.now());

        bookingRepository.save(bookingEntity);

        //publish Notification event
    }

    @Override
    @Transactional
    @KafkaListener(topics = BookingConstants.SEAT_FAILED_TOPIC)
    public void processSeatFailedEvent(ProcessingDto processingDto) {

        Optional<BookingEntity> bookingEntityOptional = bookingRepository.findById(processingDto.getBookingId());

        BookingEntity bookingEntity = bookingEntityOptional.get();

        bookingEntity.setBookingStatus(BookingConstants.BOOKING_STATUS_FAILED);
        bookingEntity.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(bookingEntity);

        try {
            bookingOutboxService.publishBookingFailedEvent(processingDto, BookingConstants.BOOKING_PENDING_CONFIRMATION_EVENT);
        }catch (Exception e){

        }


    }
}
