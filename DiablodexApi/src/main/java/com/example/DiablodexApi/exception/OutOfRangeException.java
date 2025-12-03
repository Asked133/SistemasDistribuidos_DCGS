package com.example.DiablodexApi.exception;

public class OutOfRangeException extends RuntimeException {
    public OutOfRangeException(String message) {
        super(message);
    }
    
    public OutOfRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
