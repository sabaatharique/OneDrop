package com.LesMiserables.OneDrop.authentication.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExceptionResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public ExceptionResponse(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
