package org.mahedi.core.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductReserveCancelCommand {
    private UUID orderId;
    private Map<UUID, Integer> orderedProducts;
}
