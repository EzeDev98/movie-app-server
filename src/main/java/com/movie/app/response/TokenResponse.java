package com.movie.app.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
    private String message;
    private int status;
    private boolean isSuccess;

    public TokenResponse(String message, int status, boolean isSuccess) {
        this.message = message;
        this.status = status;
        this.isSuccess = isSuccess;
    }
}
