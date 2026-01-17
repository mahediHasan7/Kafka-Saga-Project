package org.mahedi.orderservice.saga;

import org.mahedi.core.events.*;
import org.mahedi.core.types.OrderStatus;
import org.mahedi.orderservice.service.OrderHistoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@KafkaListener(topics = {"${order-event-topic-name}", "${product-event-topic-name}", "${payment-event-topic-name}"}, containerFactory = "kafkaListenerContainerFactory")
public class OrderSaga {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OrderHistoryService orderHistoryService;
    private final String productCommandTopicName;
    private final String paymentCommandTopicName;
    private final String orderCommandTopicName;

    public OrderSaga(KafkaTemplate<String, Object> kafkaTemplate,
                     @Value("${product-command-topic-name}") String productCommandTopicName,
                     @Value("${payment-command-topic-name}") String paymentCommandTopicName,
                     @Value("${order-command-topic-name}") String orderCommandTopicName,
                     OrderHistoryService orderHistoryService) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderHistoryService = orderHistoryService;
        this.productCommandTopicName = productCommandTopicName;
        this.paymentCommandTopicName = paymentCommandTopicName;
        this.orderCommandTopicName = orderCommandTopicName;
    }

    @KafkaHandler
    @Transactional(value = "transactionManager")
    public void handleEvent(@Payload OrderCreatedEvent orderCreatedEvent) {
        ProductReserveCommand productReserveCommand = new ProductReserveCommand(orderCreatedEvent.getOrderId(), orderCreatedEvent.getOrderedProducts(), orderCreatedEvent.getTotalAmount());
        kafkaTemplate.send(productCommandTopicName, productReserveCommand);
        orderHistoryService.add(orderCreatedEvent.getOrderId(), OrderStatus.CREATED);
    }

    @KafkaHandler
    @Transactional(value = "transactionManager")
    public void handleEvent(@Payload ProductReserveConfirmationEvent orderCreatedEvent) {
        PaymentProcessCommand paymentProcessCommand = new PaymentProcessCommand();
        paymentProcessCommand.setOrderId(orderCreatedEvent.getOrderId());
        paymentProcessCommand.setOrderedProducts(orderCreatedEvent.getReservedProducts());
        paymentProcessCommand.setTotalAmount(orderCreatedEvent.getOrderAmount());
        kafkaTemplate.send(paymentCommandTopicName, paymentProcessCommand);
    }

    @KafkaHandler
    @Transactional(value = "transactionManager")
    public void handleEvent(@Payload PaymentProcessSuccessfulEvent paymentProcessSuccessfulEvent) {
        OrderApprovedCommand orderApprovedCommand = new OrderApprovedCommand();
        orderApprovedCommand.setOrderId(paymentProcessSuccessfulEvent.getOrderId());
        kafkaTemplate.send(orderCommandTopicName, orderApprovedCommand);
    }

    @KafkaHandler
    @Transactional(value = "transactionManager")
    public void handleEvent(@Payload OrderApprovedEvent orderApprovedEvent) {
        orderHistoryService.add(orderApprovedEvent.getOrderId(), orderApprovedEvent.getOrderStatus());
    }

    @KafkaHandler
    @Transactional(value = "transactionManager")
    public void handleEvent(@Payload ProductReserveCancelledEvent productReserveCancelledEvent) {
        orderHistoryService.add(productReserveCancelledEvent.getOrderId(), OrderStatus.REJECTED);
        OrderRejectCommand orderRejectCommand = new OrderRejectCommand(productReserveCancelledEvent.getOrderId(), OrderStatus.REJECTED);
        kafkaTemplate.send(orderCommandTopicName, orderRejectCommand);
    }

    @KafkaHandler
    @Transactional(value = "transactionManager")
    public void handleEvent(@Payload PaymentFailedEvent paymentFailedEvent) {
        ProductReserveCancelCommand productReserveCancelCommand = new ProductReserveCancelCommand(
                paymentFailedEvent.getOrderId(),
                paymentFailedEvent.getOrderedProducts()
        );
        kafkaTemplate.send(productCommandTopicName, productReserveCancelCommand);
    }
}
