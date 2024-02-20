package com.example.fromfridgetoplate.logic.exceptions;

public class DeliveryRetrievalException extends Exception {
    public DeliveryRetrievalException(String message) {
        super(message);
    }

    public DeliveryRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
