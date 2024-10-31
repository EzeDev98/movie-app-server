package com.movie.app.exception;

public class IncorrectPinException extends IllegalArgumentException{
    public IncorrectPinException(String message) {
        super(message);
    }
}
