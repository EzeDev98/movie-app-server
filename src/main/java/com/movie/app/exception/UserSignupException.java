package com.movie.app.exception;

public class UserSignupException extends RuntimeException {
    public UserSignupException(String message, Exception ex) {
        super(message,ex);
    }
}
