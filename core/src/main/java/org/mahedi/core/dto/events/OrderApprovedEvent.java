package org.mahedi.core.dto.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mahedi.core.types.OrderStatus;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderApprovedEvent {
    private UUID orderId;
    private OrderStatus orderStatus;
}