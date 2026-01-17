package org.mahedi.orderservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${order-event-topic-name}")
    private String orderEventTopic;

    @Value("${order-command-topic-name}")
    private String orderCommandTopic;

    @Value("${product-command-topic-name}")
    private String productCommandTopic;

    @Value("${payment-command-topic-name}")
    private String paymentCommandTopic;

    @Bean
    NewTopic createOrderEventTopic() {
        return TopicBuilder.name(orderEventTopic)
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    NewTopic createOrderCommandTopic() {
        return TopicBuilder.name(orderCommandTopic)
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    NewTopic createProductCommandTopic() {
        return TopicBuilder.name(productCommandTopic)
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    NewTopic createPaymentCommandTopic() {
        return TopicBuilder.name(paymentCommandTopic)
                .partitions(3)
                .replicas(3)
                .build();
    }
}
