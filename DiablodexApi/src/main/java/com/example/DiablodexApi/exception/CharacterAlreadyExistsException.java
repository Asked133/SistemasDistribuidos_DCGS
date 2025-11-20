package com.example.DiablodexApi.exception;

public class CharacterAlreadyExistsException extends RuntimeException {
    public CharacterAlreadyExistsException(String message) {
        super(message);
    }
}
