package com.movie.app.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RegistrationRequest {
    private String fullName;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private String confirmPassword;
}
