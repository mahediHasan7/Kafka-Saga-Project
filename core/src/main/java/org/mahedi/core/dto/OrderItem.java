package org.mahedi.core.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class OrderItem {
    private UUID orderItemId;
    private UUID productId;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
}
