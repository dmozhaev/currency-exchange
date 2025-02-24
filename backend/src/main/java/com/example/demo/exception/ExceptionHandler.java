package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleValidationException(Exception ex) {
        logger.error("Exception caught in handler: {}", ex.getMessage(), ex);

        String errorMessage = ex.getMessage();
        HttpStatus status =
                switch (ex) {
                    case IllegalArgumentException illegalArgumentException ->
                            HttpStatus.BAD_REQUEST;
                    case SecurityException securityException -> HttpStatus.BAD_REQUEST;
                    case IllegalStateException illegalStateException -> HttpStatus.BAD_REQUEST;
                    default -> HttpStatus.INTERNAL_SERVER_ERROR;
                };

        return ResponseEntity.status(status).body(errorMessage);
    }
}
