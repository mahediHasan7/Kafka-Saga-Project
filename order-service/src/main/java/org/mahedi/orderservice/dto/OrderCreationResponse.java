package org.mahedi.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mahedi.core.types.OrderStatus;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationResponse {
    private UUID orderId;
    private UUID customerId;
    private OrderStatus status;
    private List<OrderItemResponse> orderItems;
}

/*
    {

      "orderId": "123e4567-e89b-12d3-a456-426614114141",
      "customerId": "123e4567-e89b-12d3-a456-426614174000",
      "status": "CREATED",
      "orderItems": [
        {
          "orderItemId": "550e8400-e29b-41d4-a716-44666666333",
          "productId": "550e8400-e29b-41d4-a716-446655440000",
          "quantity": 2,
          "priceAtPurchase": 29.99
        },
        {
          "orderItemId": "550e8400-e29b-41d4-a716-44688998998",
          "productId": "660e8400-e29b-41d4-a716-446655440001",
          "quantity": 1,
          "priceAtPurchase": 49.99
        }
      ]
    }
 */