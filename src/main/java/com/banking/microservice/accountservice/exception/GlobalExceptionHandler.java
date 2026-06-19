package com.banking.microservice.accountservice.exception;



import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;



@RestControllerAdvice
public class GlobalExceptionHandler {
        s@ExceptionHandler(AccountNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleAccountNotFound(
                AccountNotFoundException ex,
                HttpServletRequest request
        ) {
            return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI());
        }

        @ExceptionHandler(InvalidAccountStatusException.class)
        public ResponseEntity<ErrorResponse> handleInvalidStatus(
                InvalidAccountStatusException ex,
                HttpServletRequest request
        ) {
            return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
        }

        @ExceptionHandler(InsufficientBalanceException.class)
        public ResponseEntity<ErrorResponse> handleBalance(
                InsufficientBalanceException ex,
                HttpServletRequest request
        ) {
            return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneric(
                Exception ex,
                HttpServletRequest request
        ) {
            return buildErrorResponse(
                    "Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    request.getRequestURI()
            );
        }

        private ResponseEntity<ErrorResponse> buildErrorResponse(
                String message,
                HttpStatus status,
                String path
        ) {
            ErrorResponse error = ErrorResponse.builder()
                    .message(message)
                    .status(status.value())
                    .timestamp(LocalDateTime.now())
                    .path(path)
                    .build();

            return new ResponseEntity<>(error, status);
        }

}
