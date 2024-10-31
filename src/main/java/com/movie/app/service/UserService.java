package com.movie.app.service;

import com.movie.app.dto.RegistrationRequest;
import com.movie.app.response.BaseResponse;

public interface UserService {
    BaseResponse signUp(RegistrationRequest registrationRequest);
}
