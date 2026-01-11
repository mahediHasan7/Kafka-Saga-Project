package org.mahedi.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"order_id", "product_id"}))
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Many OrderItems belongs to one Order
    // This is the OWNING SIDE - it has the foreign key (order_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // snapshot: To store product price during the order placement
    @Column(name = "price_at_purchase", nullable = false)
    private BigDecimal priceAtPurchase;
}
