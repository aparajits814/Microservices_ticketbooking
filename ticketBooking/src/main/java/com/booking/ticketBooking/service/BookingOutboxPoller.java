package com.booking.ticketBooking.service;

import com.booking.ticketBooking.dto.ProcessingDto;
import com.booking.ticketBooking.entity.BookingCompensationOutboxEntity;
import com.booking.ticketBooking.entity.BookingSeatOutboxEntity;
import com.booking.ticketBooking.repository.BookingCompensationOutboxRepository;
import com.booking.ticketBooking.repository.BookingSeatOutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookingOutboxPoller {

    private KafkaTemplate<String, ProcessingDto> kafkaTemplate;

    private BookingSeatOutboxRepository bookingSeatOutboxRepository;

    private BookingCompensationOutboxRepository bookingCompensationOutboxRepository;

    private ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 500)
    public void publishSuccessOutbox(){

        List<BookingSeatOutboxEntity> bookingSeatEntities =  bookingSeatOutboxRepository.findByProcessed(false);
        for(BookingSeatOutboxEntity bookingSeatOutboxEntity : bookingSeatEntities){
            ProcessingDto processingDto;
            try {
                processingDto = objectMapper.readValue(bookingSeatOutboxEntity.getPayload(), ProcessingDto.class);
            }catch(JsonProcessingException e){
                continue;
            }

            kafkaTemplate.send(bookingSeatOutboxEntity.getTopic(),processingDto)
                    .whenComplete((result,exception)->{

                        ProcessingDto processingDtoPublished = result.getProducerRecord().value();
                        Optional<BookingSeatOutboxEntity> bookingSeatOutboxEntityOptional = bookingSeatOutboxRepository.
                                findByPaymentIdAndBookingId(processingDtoPublished.getPaymentId(), processingDtoPublished.getBookingId());

                        if(exception == null){
                            if(bookingSeatOutboxEntityOptional.isPresent()){

                                BookingSeatOutboxEntity bookingSeatOutboxEntityToUpdate = bookingSeatOutboxEntityOptional.get();
                                bookingSeatOutboxEntityToUpdate.setProcessed(true);
                                bookingSeatOutboxRepository.save(bookingSeatOutboxEntityToUpdate);
                                System.out.println("Booking processed"+processingDtoPublished.getBookingId());

                            }

                        }
                    });

        }


    }

    @Scheduled(fixedDelay = 2000)
    public void publishFailureOutbox(){

        List<BookingCompensationOutboxEntity> bookingCompensationOutboxEntities =  bookingCompensationOutboxRepository.findByProcessed(false);

        for(BookingCompensationOutboxEntity bookingCompensationOutboxEntity : bookingCompensationOutboxEntities){
            ProcessingDto processingDto;
            try {
                processingDto = objectMapper.readValue(bookingCompensationOutboxEntity.getPayload(), ProcessingDto.class);
            }catch(JsonProcessingException e){
                continue;
            }

            kafkaTemplate.send(bookingCompensationOutboxEntity.getTopic(),processingDto)
                    .whenComplete((result,exception)->{

                        ProcessingDto processingDtoPublished = result.getProducerRecord().value();
                        Optional<BookingCompensationOutboxEntity> bookingCompensationOutboxEntityOptional = bookingCompensationOutboxRepository.
                                findByPaymentIdAndBookingId(processingDtoPublished.getPaymentId(), processingDtoPublished.getBookingId());

                        if(exception == null){
                            if(bookingCompensationOutboxEntityOptional.isPresent()){

                                BookingCompensationOutboxEntity bookingCompensationOutboxUpdated = bookingCompensationOutboxEntityOptional.get();
                                bookingCompensationOutboxUpdated.setProcessed(true);
                                bookingCompensationOutboxRepository.save(bookingCompensationOutboxUpdated);
                                System.out.println("Booking failed processed"+processingDtoPublished.getBookingId());

                            }
                        }
                    });

        }


    }

}
