package com.movie.app.exception;

public class TokenAlreadyConfirmedException extends RuntimeException {
    public TokenAlreadyConfirmedException(String message) {
        super(message);
    }
}
