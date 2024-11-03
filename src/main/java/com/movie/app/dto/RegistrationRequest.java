package com.movie.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
public class RegistrationRequest {
    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("password")
    private String password;

    @JsonProperty("confirm_password")
    private String confirmPassword;


    public RegistrationRequest(String fullName, String username, String email, String phoneNumber, String password, String confirmPassword) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
