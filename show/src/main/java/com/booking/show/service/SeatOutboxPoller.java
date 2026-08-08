package com.booking.show.service;

import com.booking.show.dto.ProcessingDto;
import com.booking.show.entity.ShowSeatOutboxEntity;
import com.booking.show.repository.ShowSeatOutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
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

                        if(exception == null){
                            ProcessingDto processingDtoPublished = result.getProducerRecord().value();
                            Optional<ShowSeatOutboxEntity> bookingSeatOutboxEntityOptional = showSeatOutboxRepository.
                                    findByPaymentIdAndBookingIdAndEventType(processingDtoPublished.getPaymentId(),
                                            processingDtoPublished.getBookingId(), processingDtoPublished.getEventType());
                            if(bookingSeatOutboxEntityOptional.isPresent()){

                                ShowSeatOutboxEntity seatOutboxEntityToUpdate = bookingSeatOutboxEntityOptional.get();
                                seatOutboxEntityToUpdate.setProcessed(true);
                                showSeatOutboxRepository.save(seatOutboxEntityToUpdate);
                                log.info("Booking processed:{}", processingDtoPublished.getBookingId());

                            }

                        }
                    });

        }


    }

}
