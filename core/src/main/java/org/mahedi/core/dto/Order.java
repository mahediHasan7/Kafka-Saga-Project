package org.mahedi.core.dto;

import lombok.Getter;
import lombok.Setter;
import org.mahedi.core.types.OrderStatus;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Order {
    private UUID orderId;
    private UUID customerId;
    private OrderStatus status;
    private List<OrderItem> orderItems;
}
