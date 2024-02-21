package com.example.fromfridgetoplate.logic.exceptions;

public class DatabaseConnectionInitializationException extends RuntimeException {
    public DatabaseConnectionInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
