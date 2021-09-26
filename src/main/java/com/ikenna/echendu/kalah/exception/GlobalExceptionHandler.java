package com.ikenna.echendu.kalah.exception;

import com.ikenna.echendu.kalah.payload.ApiResponse;
import com.ikenna.echendu.kalah.payload.CreateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.ikenna.echendu.kalah.payload.CreateResponse.errorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ ApiException.class })
    public ResponseEntity<ApiResponse<CreateResponse.Response>> handleCustomException(Exception ex) {
        return errorResponse(BAD_REQUEST, ex.getLocalizedMessage());
    }
}