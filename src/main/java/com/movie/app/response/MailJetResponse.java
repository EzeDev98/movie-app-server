package com.movie.app.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailJetResponse {

    private String status;

    private int code;

    public MailJetResponse(String status, int code) {
        this.status = status;
        this.code = code;
    }
}