package com.example.fromfridgetoplate.logic.exceptions;

public class NotificationProcessingException extends Exception {


    public NotificationProcessingException(String message) {
        super(message);
    }

    // messaggio di errore + causa dell'eccezione
    public NotificationProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

}
