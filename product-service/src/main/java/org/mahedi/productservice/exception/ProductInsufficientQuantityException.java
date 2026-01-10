package org.mahedi.productservice.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductInsufficientQuantityException extends RuntimeException {
    private UUID productId;
    private UUID orderId;

    public ProductInsufficientQuantityException(UUID productId, UUID orderId) {
        super("Product " + productId + " has insufficient quantity for the order " + orderId);
        this.productId = productId;
        this.orderId = orderId;
    }
}
