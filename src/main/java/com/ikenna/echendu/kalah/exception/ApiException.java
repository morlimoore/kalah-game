package com.ikenna.echendu.kalah.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
