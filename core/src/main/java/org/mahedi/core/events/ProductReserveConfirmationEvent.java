package org.mahedi.core.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductReserveConfirmationEvent {
    private UUID orderId;
    private Map<UUID, Integer> reservedProducts;
    private BigDecimal orderAmount;
}
