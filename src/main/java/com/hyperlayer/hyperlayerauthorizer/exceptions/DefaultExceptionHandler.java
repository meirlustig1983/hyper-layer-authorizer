package com.hyperlayer.hyperlayerauthorizer.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleExceptions(Exception e, HttpServletRequest request) {
        log.error("Unhandled exception occurred", e);
        return createApiError(request, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
    private ApiError createApiError(HttpServletRequest request, String message, int statusCode) {
        return new ApiError(request.getRequestURI(), message, statusCode);
    }
}
