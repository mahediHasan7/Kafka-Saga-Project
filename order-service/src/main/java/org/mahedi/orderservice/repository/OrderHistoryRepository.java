package org.mahedi.orderservice.repository;

import org.mahedi.orderservice.entity.OrderHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderHistoryRepository extends JpaRepository<OrderHistoryEntity, UUID> {
    List<OrderHistoryEntity> getByOrderId(UUID orderId);
}
