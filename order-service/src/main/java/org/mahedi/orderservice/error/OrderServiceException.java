package org.mahedi.orderservice.error;

public class OrderServiceException extends RuntimeException {
    public OrderServiceException(String message) {
        super(message);
    }

    public OrderServiceException(Throwable cause) {
        super(cause);
    }
}
