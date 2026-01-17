package org.mahedi.orderservice.service;

import org.mahedi.core.dto.Order;
import org.mahedi.core.types.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface OrderService {
    public Order placeOrder(Order order);

    public Order changeStatus(UUID orderId, OrderStatus orderStatus);
}
