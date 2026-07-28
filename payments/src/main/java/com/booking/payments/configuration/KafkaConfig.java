package com.booking.payments.configuration;

import com.booking.payments.constants.PaymentsConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaAdmin.NewTopics bookingTopics() {
        return new KafkaAdmin.NewTopics(
                createTopic(PaymentsConstants.PAYMENT_SUCCESS_TOPIC),
                createTopic(PaymentsConstants.PAYMENT_EXPIRE_TOPIC)
        );
    }

    private NewTopic createTopic(String topicName) {
        return TopicBuilder.name(topicName)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

}
