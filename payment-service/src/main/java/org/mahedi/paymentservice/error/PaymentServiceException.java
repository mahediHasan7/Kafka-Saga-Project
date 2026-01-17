package org.mahedi.paymentservice.error;

public class PaymentServiceException extends RuntimeException {
    public PaymentServiceException(String message) {
        super(message);
    }

    public PaymentServiceException(Throwable cause) {
        super(cause);
    }
}
