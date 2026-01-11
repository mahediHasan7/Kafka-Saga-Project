package org.mahedi.orderservice.controller;

import jakarta.validation.Valid;
import org.mahedi.core.dto.Order;
import org.mahedi.core.dto.OrderItem;
import org.mahedi.core.types.OrderStatus;
import org.mahedi.orderservice.dto.OrderCreationRequest;
import org.mahedi.orderservice.dto.OrderCreationResponse;
import org.mahedi.orderservice.dto.OrderHistoryResponse;
import org.mahedi.orderservice.dto.OrderItemResponse;
import org.mahedi.orderservice.service.OrderHistoryService;
import org.mahedi.orderservice.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderHistoryService orderHistoryService;

    public OrderController(OrderService orderService, OrderHistoryService orderHistoryService) {
        this.orderService = orderService;
        this.orderHistoryService = orderHistoryService;
    }

    @PostMapping
    ResponseEntity<OrderCreationResponse> placeOrder(@RequestBody @Valid OrderCreationRequest request) {
        Order order = mapToOrder(request);
        Order savedOrder = orderService.placeOrder(order);
        OrderCreationResponse response = mapToOrderCreationResponse(savedOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}/history")
    public ResponseEntity<List<OrderHistoryResponse>> gerOrderHistory(@PathVariable UUID orderId) {
        List<OrderHistoryResponse> orderHistories = orderHistoryService.findByOrderId(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderHistories);
    }

    private static OrderCreationResponse mapToOrderCreationResponse(Order savedOrder) {
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
        return response;
    }

    private static Order mapToOrder(OrderCreationRequest request) {
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setStatus(OrderStatus.CREATED);
        List<OrderItem> orderItems = request.getOrderItems().stream().map(orderItemRequest -> {
            OrderItem orderItem = new OrderItem();
            BeanUtils.copyProperties(orderItemRequest, orderItem);
            return orderItem;
        }).toList();
        order.setOrderItems(orderItems);
        return order;
    }
}
