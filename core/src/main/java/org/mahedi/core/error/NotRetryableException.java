package org.mahedi.core.error;

public class NotRetryableException extends RuntimeException {
    public NotRetryableException(Exception exception) {
        super(exception);
    }

    public NotRetryableException(String message) {
        super(message);
    }
}
