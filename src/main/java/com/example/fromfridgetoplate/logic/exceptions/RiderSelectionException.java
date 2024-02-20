package com.example.fromfridgetoplate.logic.exceptions;

public class RiderSelectionException extends Exception {
    public RiderSelectionException(String message) {
        super(message);
    }

    public RiderSelectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
