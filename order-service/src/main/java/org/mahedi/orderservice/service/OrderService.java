package org.mahedi.orderservice.service;

import org.mahedi.core.dto.Order;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    public Order placeOrder(Order order);
}
