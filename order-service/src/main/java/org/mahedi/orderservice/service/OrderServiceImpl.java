package org.mahedi.orderservice.service;

import org.mahedi.core.dto.Order;
import org.mahedi.core.dto.OrderItem;
import org.mahedi.core.dto.events.OrderCreatedEvent;
import org.mahedi.core.types.OrderStatus;
import org.mahedi.orderservice.entity.OrderEntity;
import org.mahedi.orderservice.entity.OrderItemEntity;
import org.mahedi.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String orderEventTopicName;

    public OrderServiceImpl(OrderRepository orderRepository,
                            KafkaTemplate<String, Object> kafkaTemplate,
                            @Value("${order-event-topic-name}") String orderEventTopicName) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.orderEventTopicName = orderEventTopicName;
    }

    @Override
    @Transactional(value = "transactionManager")
    public Order placeOrder(Order order) {
        OrderEntity orderEntity = mapToOrderEntity(order);
        orderRepository.save(orderEntity);

        // Publish OrderCreatedEvent to orders-events
        Map<UUID, Integer> orderedProducts = new HashMap<>();
        order.getOrderItems().forEach(orderItem -> {
            orderedProducts.put(orderItem.getProductId(), orderItem.getQuantity());
        });

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(orderEntity.getId(), orderedProducts, getTotalOrderAmount(order));
        kafkaTemplate.send(orderEventTopicName, orderCreatedEvent);

        return mapToOrder(orderEntity);
    }

    @Override
    public Order changeStatus(UUID orderId, OrderStatus orderStatus) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow();
        orderEntity.setStatus(orderStatus);
        orderRepository.save(orderEntity);

        return mapToOrder(orderEntity);
    }


    private static BigDecimal getTotalOrderAmount(Order order) {
        return order.getOrderItems().stream()
                .map(item -> BigDecimal.valueOf(item.getQuantity()).multiply(item.getPriceAtPurchase()))
                .reduce(BigDecimal.ZERO, (acc, orderAmount) -> acc.add(orderAmount));
    }

    private static Order mapToOrder(OrderEntity orderEntity) {
        Order createdOrder = new Order();
        createdOrder.setOrderId(orderEntity.getId());
        createdOrder.setCustomerId(orderEntity.getCustomerId());
        createdOrder.setStatus(orderEntity.getStatus());
        List<OrderItem> createdOrderItems = orderEntity.getOrderItems().stream().map(orderItemEntity -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(orderItemEntity.getId());
            orderItem.setProductId(orderItemEntity.getProductId());
            orderItem.setQuantity(orderItemEntity.getQuantity());
            orderItem.setPriceAtPurchase(orderItemEntity.getPriceAtPurchase());
            return orderItem;
        }).toList();
        createdOrder.setOrderItems(createdOrderItems);
        return createdOrder;
    }

    private static OrderEntity mapToOrderEntity(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomerId(order.getCustomerId());
        orderEntity.setStatus(order.getStatus());
        List<OrderItemEntity> orderItems = order.getOrderItems().stream().map(orderItem -> {
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setOrder(orderEntity);// set the bidirectional relationship
            orderItemEntity.setProductId(orderItem.getProductId());
            orderItemEntity.setQuantity(orderItem.getQuantity());
            orderItemEntity.setPriceAtPurchase(orderItem.getPriceAtPurchase());
            return orderItemEntity;
        }).toList();
        orderEntity.setOrderItems(orderItems);
        return orderEntity;
    }
}
