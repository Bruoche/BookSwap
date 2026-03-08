package fr.bookswap.common.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    public int status;
    public String message;
    public LocalDateTime timestamp;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}