package com.booking.payments.service;

import com.booking.payments.dto.ProcessingDto;
import com.booking.payments.entity.PaymentPollerEntity;
import com.booking.payments.repository.PaymentPollerRepository;
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
public class PaymentOutboxPoller {

    private KafkaTemplate<String, ProcessingDto> kafkaTemplate;

    private PaymentPollerRepository paymentPollerRepository;

    private ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 500)
    public void publishOutbox(){

        List<PaymentPollerEntity> paymentPollerEntities =  paymentPollerRepository.findByProcessed(false);



        for(PaymentPollerEntity paymentPollerEntity : paymentPollerEntities){
            ProcessingDto processingDto;

            try {

                processingDto = objectMapper.readValue(paymentPollerEntity.getPayload(), ProcessingDto.class);

            }catch(JsonProcessingException e){

                continue;

            }

            kafkaTemplate.send(paymentPollerEntity.getTopic(),processingDto)
                    .whenComplete((result,exception)->{

                        ProcessingDto processingDtoPublished = result.getProducerRecord().value();

                        Optional<PaymentPollerEntity> paymentPollerEntityOptional = paymentPollerRepository.
                                findByPaymentIdAndBookingId(processingDtoPublished.getPaymentId(), processingDto.getBookingId());

                        if(exception == null){

                            if(paymentPollerEntityOptional.isPresent()){

                                PaymentPollerEntity paymentPollerEntityToUpdate = paymentPollerEntityOptional.get();

                                paymentPollerEntityToUpdate.setProcessed(true);

                                paymentPollerRepository.save(paymentPollerEntityToUpdate);

                            }

                        }
                    });

        }


    }


}
