package org.mahedi.orderservice.service;

import org.mahedi.core.types.OrderStatus;
import org.mahedi.orderservice.dto.OrderHistoryResponse;

import java.util.List;
import java.util.UUID;

public interface OrderHistoryService {
    List<OrderHistoryResponse> findByOrderId(UUID orderId);

    void add(UUID orderId, OrderStatus orderStatus);
}
