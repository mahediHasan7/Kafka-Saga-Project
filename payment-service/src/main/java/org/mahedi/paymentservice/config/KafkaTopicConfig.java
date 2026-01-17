package org.mahedi.paymentservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${payment-event-topic-name}")
    private String paymentEventTopic;


    @Bean
    NewTopic createPaymentEventTopic() {
        return TopicBuilder.name(paymentEventTopic)
                .partitions(3)
                .replicas(3)
                .build();
    }
}
