package com.nexusprocure.exception;

import lombok.Getter;

import java.time.LocalDateTime;
@Getter

public class ErrorResponse {
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String path;
    private String error;
    public ErrorResponse(){
    }
    public ErrorResponse(String message, int status, LocalDateTime timestamp, String path, String error ){
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        this.error = error;
        this.path = path;
    }
}
