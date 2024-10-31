package com.movie.app.utility.impl;

import com.movie.app.response.BaseResponse;
import com.movie.app.utility.ResponseService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImpl implements ResponseService {

    @Override
    public BaseResponse createSuccessResponse(BaseResponse response, String message) {
        response.setDescription(message);
        response.setStatusCode(HttpServletResponse.SC_OK);
        return response;
    }

    @Override
    public BaseResponse createErrorResponse(BaseResponse response, String message, int statusCode) {
        response.setDescription(message);
        response.setStatusCode(statusCode);
        return response;
    }
}
