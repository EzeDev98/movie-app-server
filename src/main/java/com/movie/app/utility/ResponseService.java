package com.movie.app.utility;

import com.movie.app.response.BaseResponse;

public interface ResponseService {
    public static final String USER_ALREADY_EXISTS = "User already exists!";
    public static final String USERNAME_ALREADY_EXISTS = "User with this username already exists";
    public static final String PHONE_NUMBER_ALREADY_EXISTS = "User with this phone number already exists";
    public static final String EMAIL_ALREADY_EXISTS = "User with this email already exists";
    public static final String PASSWORD_MISMATCH = "Passwords do not match. Try again...";
    public static final String INVALID_PHONE_NUMBER = "Phone number must be 11 digits";
    public static final String INVALID_PASSWORD = "Password must be at least 8 characters long, " +
            "contain at least one digit, one uppercase letter, " +
            "one lowercase letter, and one special character.";
    BaseResponse createSuccessResponse(BaseResponse response, String message);
    BaseResponse createErrorResponse(BaseResponse response, String message, int statusCode);
}
