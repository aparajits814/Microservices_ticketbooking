package com.booking.show.service;

import com.booking.show.dto.ProcessingDto;
import com.booking.show.entity.ShowSeatOutboxEntity;
import com.booking.show.repository.ShowSeatOutboxRepository;
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
public class SeatOutboxPoller {

    private KafkaTemplate<String, ProcessingDto> kafkaTemplate;

    private ShowSeatOutboxRepository showSeatOutboxRepository;

    private ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 500)
    public void publishOutbox(){

        List<ShowSeatOutboxEntity> showSeatOutboxEntities =  showSeatOutboxRepository.findByProcessed(false);
        for(ShowSeatOutboxEntity showSeatOutbox : showSeatOutboxEntities){
            ProcessingDto processingDto;
            try {
                processingDto = objectMapper.readValue(showSeatOutbox.getPayload(), ProcessingDto.class);
            }catch(JsonProcessingException e){
                continue;
            }

            kafkaTemplate.send(showSeatOutbox.getTopic(),processingDto)
                    .whenComplete((result,exception)->{

                        ProcessingDto processingDtoPublished = result.getProducerRecord().value();
                        Optional<ShowSeatOutboxEntity> bookingSeatOutboxEntityOptional = showSeatOutboxRepository.
                                findByPaymentIdAndBookingIdAndEventType(processingDtoPublished.getPaymentId(),
                                        processingDtoPublished.getBookingId(), processingDtoPublished.getEventType());

                        if(exception == null){
                            if(bookingSeatOutboxEntityOptional.isPresent()){

                                ShowSeatOutboxEntity seatOutboxEntityToUpdate = bookingSeatOutboxEntityOptional.get();
                                seatOutboxEntityToUpdate.setProcessed(true);
                                showSeatOutboxRepository.save(seatOutboxEntityToUpdate);
                                System.out.println("Booking processed"+processingDtoPublished.getBookingId());

                            }

                        }
                    });

        }


    }

}
