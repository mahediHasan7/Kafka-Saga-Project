package org.mahedi.orderservice.controller;

import jakarta.validation.Valid;
import org.mahedi.core.dto.Order;
import org.mahedi.core.dto.OrderItem;
import org.mahedi.core.types.OrderStatus;
import org.mahedi.orderservice.dto.OrderCreationRequest;
import org.mahedi.orderservice.dto.OrderCreationResponse;
import org.mahedi.orderservice.dto.OrderItemResponse;
import org.mahedi.orderservice.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    ResponseEntity<OrderCreationResponse> createOrder(@RequestBody @Valid OrderCreationRequest request) {
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setStatus(OrderStatus.CREATED);
        List<OrderItem> orderItems = request.getOrderItems().stream().map(orderItemRequest -> {
            OrderItem orderItem = new OrderItem();
            BeanUtils.copyProperties(orderItemRequest, orderItem);
            return orderItem;
        }).toList();
        order.setOrderItems(orderItems);

        Order savedOrder = orderService.create(order);

        OrderCreationResponse response = new OrderCreationResponse();
        response.setOrderId(savedOrder.getOrderId());
        response.setCustomerId(savedOrder.getCustomerId());
        response.setStatus(savedOrder.getStatus());
        List<OrderItemResponse> savedOrderItems = savedOrder.getOrderItems().stream().map(orderItem -> {
            OrderItemResponse orderItemResponse = new OrderItemResponse();
            BeanUtils.copyProperties(orderItem, orderItemResponse);
            return orderItemResponse;
        }).toList();
        response.setOrderItems(savedOrderItems);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
