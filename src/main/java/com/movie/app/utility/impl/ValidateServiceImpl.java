package com.movie.app.utility.impl;

import com.movie.app.exception.IncorrectPinException;
import com.movie.app.response.BaseResponse;
import com.movie.app.utility.ValidateService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ValidateServiceImpl implements ValidateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateServiceImpl.class);
    @Override
    public void validatePin(Integer actualWalletPin, Integer pinProvided) {

        if (!actualWalletPin.equals(pinProvided)) {
            LOGGER.error("Pin provided does not match the correct pin");
            throw new IncorrectPinException("The pin provided does not match the correct pin");
        }
    }

    @Override
    public boolean validateRequiredFields(BaseResponse response, Object... fields) {
        for (Object field : fields) {
            if (field == null) {
                LOGGER.error("One of more fields are null");
                response.setDescription("All required fields must not be null");
                response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                return false;
            }

            if (field instanceof String && ((String) field).isEmpty()) {
                LOGGER.error("One or more fields are null");
                response.setDescription("All required fields must not be null");
                response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\\d{11}");
    }
}
