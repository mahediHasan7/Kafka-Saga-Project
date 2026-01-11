package org.mahedi.orderservice.service;

import org.mahedi.core.dto.Order;
import org.mahedi.core.dto.OrderItem;
import org.mahedi.orderservice.entity.OrderEntity;
import org.mahedi.orderservice.entity.OrderItemEntity;
import org.mahedi.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order create(Order order) {
        OrderEntity orderEntity = mapToOrderEntity(order);
        orderRepository.save(orderEntity);
        return mapToOrder(orderEntity);
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
