package org.mahedi.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mahedi.core.types.OrderStatus;

import java.sql.Timestamp;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryResponse {
    private UUID id;
    private UUID orderId;
    private OrderStatus status;
    private Timestamp createdAt;
}
