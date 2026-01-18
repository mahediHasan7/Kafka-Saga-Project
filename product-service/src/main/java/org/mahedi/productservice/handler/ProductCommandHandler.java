package org.mahedi.productservice.handler;

import org.mahedi.core.dto.commands.ProductReserveCancelCommand;
import org.mahedi.core.dto.commands.ProductReserveCommand;
import org.mahedi.core.dto.events.ProductReserveCancelledEvent;
import org.mahedi.core.dto.events.ProductReserveConfirmationEvent;
import org.mahedi.productservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@KafkaListener(topics = {"${product-command-topic-name}"}, containerFactory = "kafkaListenerContainerFactory")
public class ProductCommandHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(ProductCommandHandler.class);
    private final ProductService productService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String productEventsTopicName;


    public ProductCommandHandler(ProductService productService, KafkaTemplate<String, Object> kafkaTemplate, @Value("${product-event-topic-name}") String productEventsTopicName) {
        this.productService = productService;
        this.kafkaTemplate = kafkaTemplate;
        this.productEventsTopicName = productEventsTopicName;
    }

    @KafkaHandler
    @Transactional(value = "transactionManager")
    public void productReserveHandler(@Payload ProductReserveCommand productReserveCommand) {
        try {
            // reserving each product
            productReserveCommand.getOrderedProducts().forEach((productId, quantity) -> {
                productService.reserve(productReserveCommand.getOrderId(), productId, quantity);
            });
            // sending product reserve event to products-events
            ProductReserveConfirmationEvent productReserveConfirmationEvent = new ProductReserveConfirmationEvent(productReserveCommand.getOrderId(), productReserveCommand.getOrderedProducts(), productReserveCommand.getOrderAmount());

            kafkaTemplate.send(productEventsTopicName, productReserveConfirmationEvent);
        } catch (Exception e) {
            // Compensating Path: when product reservation failed
            LOGGER.error(e.getLocalizedMessage(), e);
            ProductReserveCancelledEvent productReserveCancelledEvent = new ProductReserveCancelledEvent();
            kafkaTemplate.executeInTransaction(kafkaOperation -> {
                productReserveCancelledEvent.setOrderId(productReserveCommand.getOrderId());
                kafkaOperation.send(productEventsTopicName, productReserveCancelledEvent);
                return true;
            });
        }
    }


    @KafkaHandler
    @Transactional(value = "transactionManager")
    public void productReserveCancelHandler(@Payload ProductReserveCancelCommand productReserveCancelCommand) {
        try {
            // reverse reserving each product
            productReserveCancelCommand.getOrderedProducts().forEach(productService::cancelReservation);

            // sending product reserve cancelled event
            kafkaTemplate.send(productEventsTopicName, new ProductReserveCancelledEvent(productReserveCancelCommand.getOrderId()));

        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
    }
}
