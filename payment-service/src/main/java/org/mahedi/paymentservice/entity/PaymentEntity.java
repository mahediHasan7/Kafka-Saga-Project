package org.mahedi.paymentservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Table(name = "payments")
@Entity
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "total_price")
    private BigDecimal paymentAmount;
}
