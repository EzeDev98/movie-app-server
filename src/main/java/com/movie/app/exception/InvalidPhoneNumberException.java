package com.movie.app.exception;

public class InvalidPhoneNumberException extends IllegalArgumentException {
    public InvalidPhoneNumberException(String message) {
        super(message);
    }
}
