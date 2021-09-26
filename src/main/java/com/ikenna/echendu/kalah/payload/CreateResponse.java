package com.ikenna.echendu.kalah.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateResponse {

    public static <T> ResponseEntity<ApiResponse<T>> createResponse(ApiResponse<T> response) {
        return new ResponseEntity<>(response, response.getStatus());
    }

    public static ResponseEntity<ApiResponse<Error>> errorResponse(HttpStatus status, String result) {
        ApiResponse<Error> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMessage("ERROR");
        response.setResult(new Error(result));
        return createResponse(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> errorResponse(HttpStatus status, T result) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMessage("ERROR");
        response.setResult(result);
        return createResponse(response);
    }

    public static ResponseEntity<ApiResponse<Success>> successResponse(HttpStatus status, String result) {
        ApiResponse<Success> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMessage("SUCCESS");
        response.setResult(new Success(result));
        return createResponse(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> successResponse(HttpStatus status, T result) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMessage("SUCCESS");
        response.setResult(result);
        return createResponse(response);
    }

    @Data
    @AllArgsConstructor
    public static class Error {
        private String error;
    }

    @Data
    @AllArgsConstructor
    public static class Success {
        private String success;
    }
}