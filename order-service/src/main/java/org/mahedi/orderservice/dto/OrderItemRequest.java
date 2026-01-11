package org.mahedi.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    @NotNull
    private UUID productId;
    @Positive
    @NotNull
    private Integer quantity;
    @Positive
    @NotNull
    private BigDecimal priceAtPurchase;
}