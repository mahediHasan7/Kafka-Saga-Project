package org.mahedi.orderservice.handler;

import org.mahedi.core.dto.Order;
import org.mahedi.core.events.OrderApprovedCommand;
import org.mahedi.core.events.OrderApprovedEvent;
import org.mahedi.core.events.OrderRejectCommand;
import org.mahedi.core.types.OrderStatus;
import org.mahedi.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@KafkaListener(topics = {"${order-command-topic-name}"}, containerFactory = "kafkaListenerContainerFactory")
public class OrderCommandsHandler {
    private final OrderService orderService;
    private final String orderEventTopicName;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderCommandsHandler(OrderService orderService,
                                KafkaTemplate<String, Object> kafkaTemplate,
                                @Value("${order-event-topic-name}") String orderEventTopicName) {
        this.orderService = orderService;
        this.kafkaTemplate = kafkaTemplate;
        this.orderEventTopicName = orderEventTopicName;
    }

    @KafkaHandler
    @Transactional(value = "transactionManager")
    public void handleCommand(@Payload OrderApprovedCommand orderApprovedCommand) {
        Order order = orderService.changeStatus(orderApprovedCommand.getOrderId(), OrderStatus.APPROVED);
        OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(order.getOrderId(), order.getStatus());
        kafkaTemplate.send(orderEventTopicName, orderApprovedEvent);
    }

    @KafkaHandler
    @Transactional(value = "transactionManager")
    public void handleCommand(@Payload OrderRejectCommand orderRejectCommand) {
        orderService.changeStatus(orderRejectCommand.getOrderId(), orderRejectCommand.getOrderStatus());
    }
}
