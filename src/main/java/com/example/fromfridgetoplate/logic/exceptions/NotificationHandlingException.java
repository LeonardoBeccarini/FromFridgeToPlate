package com.example.fromfridgetoplate.logic.exceptions;

public class NotificationHandlingException extends Exception {
    public NotificationHandlingException(String message) {
        super(message);
    }

    public NotificationHandlingException(String message, Throwable cause) {
        super(message, cause);
    }
}
