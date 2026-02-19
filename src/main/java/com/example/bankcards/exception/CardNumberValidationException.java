package com.example.bankcards.exception;

public class CardNumberValidationException extends RuntimeException {
    public CardNumberValidationException(String message) {
        super(message);
    }
}
