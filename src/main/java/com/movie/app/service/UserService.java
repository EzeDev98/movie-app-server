package com.movie.app.service;

import com.movie.app.dto.RegistrationRequest;
import com.movie.app.dto.UserRequest;
import com.movie.app.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<BaseResponse> signUp(RegistrationRequest registrationRequest);
    BaseResponse login(UserRequest userRequest);
}
