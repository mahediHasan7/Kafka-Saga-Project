package org.mahedi.productservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${product-event-topic-name}")
    private String productEventTopic;


    @Bean
    NewTopic createProductEventTopic() {
        return TopicBuilder.name(productEventTopic)
                .partitions(3)
                .replicas(3)
                .build();
    }
}
