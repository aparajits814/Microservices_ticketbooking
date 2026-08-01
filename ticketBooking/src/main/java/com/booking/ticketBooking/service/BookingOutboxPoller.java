package com.booking.ticketBooking.service;

import com.booking.ticketBooking.dto.ProcessingDto;
import com.booking.ticketBooking.entity.BookingSeatOutboxEntity;
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

    private ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 500)
    public void publishOutbox(){

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
                                findByPaymentIdAndBookingIdAndEventType(processingDtoPublished.getPaymentId(),
                                        processingDtoPublished.getBookingId(), processingDtoPublished.getEventType());

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

}
