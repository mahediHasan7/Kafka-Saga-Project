package org.mahedi.core.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderCreatedEvent {
    private UUID orderId;
    private BigDecimal totalPrice;
}
