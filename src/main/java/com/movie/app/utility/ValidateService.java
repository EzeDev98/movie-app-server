package com.movie.app.utility;

import com.movie.app.response.BaseResponse;

public interface ValidateService {
    void validatePin(Integer actualWalletPin, Integer pinProvided);
    boolean validateRequiredFields(BaseResponse response, Object... fields);
    boolean validatePhoneNumber(String phoneNumber);
}
