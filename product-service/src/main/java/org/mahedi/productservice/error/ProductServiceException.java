package org.mahedi.productservice.error;

import lombok.Getter;

import java.util.UUID;

public class ProductServiceException extends RuntimeException {
    public ProductServiceException(String message) {
        super(message);
    }

    public ProductServiceException(Throwable cause) {
        super(cause);
    }

    @Getter
    public static class ProductInsufficientQuantityException extends RuntimeException {
        private UUID productId;
        private UUID orderId;

        public ProductInsufficientQuantityException(UUID productId, UUID orderId) {
            super("Product " + productId + " has insufficient quantity for the order " + orderId);
            this.productId = productId;
            this.orderId = orderId;
        }
    }
}
