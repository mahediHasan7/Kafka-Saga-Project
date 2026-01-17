package org.mahedi.paymentservice.handler;

import org.mahedi.core.events.OrderCreatedEvent;
import org.mahedi.core.events.PaymentFailedEvent;
import org.mahedi.core.events.PaymentProcessCommand;
import org.mahedi.core.events.PaymentProcessSuccessfulEvent;
import org.mahedi.paymentservice.service.PaymentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@KafkaListener(topics = {"${payment-command-topic-name}"}, containerFactory = "kafkaListenerContainerFactory")
public class PaymentCommandHandler {
    private final PaymentService paymentService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String paymentEventsTopicName;

    public PaymentCommandHandler(PaymentService paymentService,
                                 KafkaTemplate<String, Object> kafkaTemplate,
                                 @Value("${payment-event-topic-name}") String paymentEventsTopicName) {
        this.paymentService = paymentService;
        this.kafkaTemplate = kafkaTemplate;
        this.paymentEventsTopicName = paymentEventsTopicName;
    }

    @KafkaHandler
    @Transactional(value = "transactionManager")
    public void paymentProcessHandler(@Payload PaymentProcessCommand paymentProcessCommand) {
        try {
            OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
            BeanUtils.copyProperties(paymentProcessCommand, orderCreatedEvent);
            paymentService.process(orderCreatedEvent);

            PaymentProcessSuccessfulEvent paymentProcessSuccessfulEvent = new PaymentProcessSuccessfulEvent();
            BeanUtils.copyProperties(paymentProcessCommand, paymentProcessSuccessfulEvent);
            kafkaTemplate.send(paymentEventsTopicName, paymentProcessSuccessfulEvent);
        } catch (Exception e) {
            // Compensating path: when payment failed
            kafkaTemplate.executeInTransaction(kafkaOperations -> {
                PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(
                        paymentProcessCommand.getOrderId(),
                        paymentProcessCommand.getOrderedProducts()
                );
                kafkaOperations.send(paymentEventsTopicName, paymentFailedEvent);
                return true;
            });
        }
    }
}
