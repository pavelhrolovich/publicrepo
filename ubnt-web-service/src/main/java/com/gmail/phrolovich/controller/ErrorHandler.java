package com.gmail.phrolovich.controller;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@Component
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> conflict(IllegalArgumentException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null) {
            body = new ApiError(status, "Error processing", ex.getMessage());
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }


    @Data
    @ApiModel("ApiError")
    public static class ApiError {
        private HttpStatus httpStatus;
        private String message;
        private String messageDetails;
        private String dateTime;

        ApiError(HttpStatus httpStatus, String message) {
            this(httpStatus, message, null);
        }

        ApiError(HttpStatus httpStatus, String message, String messageDetails) {
            this.httpStatus = httpStatus;
            this.message = message;
            this.messageDetails = messageDetails;
            this.dateTime = Instant.now().toString();
        }
    }
}
