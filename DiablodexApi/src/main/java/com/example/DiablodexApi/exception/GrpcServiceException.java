package com.example.DiablodexApi.exception;

public class GrpcServiceException extends RuntimeException {
    public GrpcServiceException(String message) {
        super(message);
    }

    public GrpcServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
