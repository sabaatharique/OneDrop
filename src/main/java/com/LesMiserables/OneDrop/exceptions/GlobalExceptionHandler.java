package com.LesMiserables.OneDrop.exceptions;

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

    @ExceptionHandler(UnauthorisedAdminActionException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorisedAdmin(
            UnauthorisedAdminActionException ex, HttpServletRequest request ) {
        return new ResponseEntity<>(
                new ExceptionResponse(HttpStatus.FORBIDDEN.value(),
                        "Unauthorised Admin Login Action",
                        ex.getMessage(),
                        request.getRequestURI()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRequestNotFound(
            RequestNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                new ExceptionResponse(HttpStatus.NOT_FOUND.value(),
                        "Request Not Found",
                        ex.getMessage(),
                        request.getRequestURI()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DonorNotEligibleException.class)
    public ResponseEntity<ExceptionResponse> handleDonorNotEligible(
            DonorNotEligibleException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                        "Donor Not Eligible",
                        ex.getMessage(),
                        request.getRequestURI()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRequestActionException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidRequestAction(
            InvalidRequestActionException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                        "Invalid Request Action",
                        ex.getMessage(),
                        request.getRequestURI()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DonorNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleDonorNotFound(DonorNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                new ExceptionResponse(HttpStatus.NOT_FOUND.value(),
                        "Donor Not Found",
                        ex.getMessage(),
                        request.getRequestURI()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(RecipientNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRecipientNotFound(RecipientNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                new ExceptionResponse(HttpStatus.NOT_FOUND.value(),
                        "Recipient Not Found",
                        ex.getMessage(),
                        request.getRequestURI()),
                HttpStatus.NOT_FOUND
        );
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
