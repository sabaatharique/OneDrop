package com.LesMiserables.OneDrop.authentication.exception;

import com.LesMiserables.OneDrop.authentication.dto.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFound(
            UserNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                new ExceptionResponse(HttpStatus.NOT_FOUND.value(),
                        "User Not Found",
                        ex.getMessage(),
                        request.getRequestURI()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidCredentials(
            InvalidCredentialsException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(),
                        "Invalid Credentials",
                        ex.getMessage(),
                        request.getRequestURI()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorisedActionException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorisedAdmin(
            UnauthorisedActionException ex, HttpServletRequest request ) {
        return new ResponseEntity<>(
                new ExceptionResponse(HttpStatus.FORBIDDEN.value(),
                        "Unauthorised Login Action",
                        ex.getMessage(),
                        request.getRequestURI()),
                HttpStatus.FORBIDDEN);
    }

    // default exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneralError(
            Exception ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal Server Error",
                        ex.getMessage(),
                        request.getRequestURI()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
