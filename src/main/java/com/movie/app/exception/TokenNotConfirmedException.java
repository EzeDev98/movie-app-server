package com.movie.app.exception;

public class TokenNotConfirmedException extends RuntimeException {
    public TokenNotConfirmedException(String message) {
        super(message);
    }
}
