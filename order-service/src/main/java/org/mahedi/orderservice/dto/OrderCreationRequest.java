package org.mahedi.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationRequest {
    @NotNull
    private UUID customerId;
    @NotNull
    private List<OrderItemRequest> orderItems;
}

/*
    {
      "customerId": "123e4567-e89b-12d3-a456-426614174000",
      "status": "CREATED",
      "orderItems": [
        {
          "productId": "550e8400-e29b-41d4-a716-446655440000",
          "quantity": 2,
          "priceAtPurchase": 29.99
        },
        {
          "productId": "660e8400-e29b-41d4-a716-446655440001",
          "quantity": 1,
          "priceAtPurchase": 49.99
        }
      ]
    }
 */