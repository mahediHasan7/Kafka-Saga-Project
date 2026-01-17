package org.mahedi.core.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderCreatedEvent {
    private UUID orderId;
    private Map<UUID, Integer> orderedProducts;
    private BigDecimal totalAmount;
}
