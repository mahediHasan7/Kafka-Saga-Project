package org.mahedi.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mahedi.core.types.OrderStatus;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders_history")
public class OrderHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "oder_id")
    @NotNull
    private UUID orderId;

    @Column(name = "status")
    @NotNull
    private OrderStatus status;

    @Column(name = "created_at")
    @NotNull
    private Timestamp createdAt;
}
