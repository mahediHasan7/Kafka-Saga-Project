package org.mahedi.orderservice.service;

import org.mahedi.core.types.OrderStatus;
import org.mahedi.orderservice.dto.OrderHistoryResponse;
import org.mahedi.orderservice.entity.OrderHistoryEntity;
import org.mahedi.orderservice.repository.OrderHistoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;

    public OrderHistoryServiceImpl(OrderHistoryRepository orderHistoryRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
    }

    @Override
    public List<OrderHistoryResponse> findByOrderId(UUID orderId) {
        return orderHistoryRepository.getByOrderId(orderId).stream().map(orderHistoryEntity -> {
            OrderHistoryResponse orderHistoryResponse = new OrderHistoryResponse();
            BeanUtils.copyProperties(orderHistoryEntity, orderHistoryResponse);
            return orderHistoryResponse;
        }).toList();
    }

    @Override
    public void add(UUID orderId, OrderStatus orderStatus) {
        OrderHistoryEntity orderHistoryEntity = new OrderHistoryEntity();
        orderHistoryEntity.setOrderId(orderId);
        orderHistoryEntity.setStatus(orderStatus);
        orderHistoryEntity.setCreatedAt(new Timestamp(new Date().getTime()));

        orderHistoryRepository.save(orderHistoryEntity);
    }
}
