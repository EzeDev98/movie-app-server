package com.movie.app.exception;

public class PasswordMismatchException extends IllegalArgumentException{
    public PasswordMismatchException(String message) {
        super(message);
    }
}
