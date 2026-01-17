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
@NoArgsConstructor
@AllArgsConstructor
public class ProductReserveCommand {
    private UUID orderId;
    private Map<UUID, Integer> orderedProducts;
    private BigDecimal orderAmount;
}
