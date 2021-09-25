package com.ikenna.echendu.kalah.payload;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateResponse {

    public static <T> ResponseEntity<ApiResponse<T>> createResponse(ApiResponse<T> response) {
        return new ResponseEntity<>(response, response.getStatus());
    }

    public static <T> ResponseEntity<ApiResponse<T>> errorResponse(T result, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMessage("ERROR");
        response.setResult(result);
        return createResponse(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> successResponse(T result, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMessage("SUCCESS");
        response.setResult(result);
        return createResponse(response);
    }
}