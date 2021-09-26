package com.ikenna.echendu.kalah.service;

import com.ikenna.echendu.kalah.dto.request.LoginRequest;
import com.ikenna.echendu.kalah.dto.request.SignUpRequest;
import com.ikenna.echendu.kalah.payload.ApiResponse;
import com.ikenna.echendu.kalah.payload.CreateResponse;
import com.ikenna.echendu.kalah.payload.JwtResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<ApiResponse<CreateResponse.Response>> createUserAccount(SignUpRequest signUpRequest);

    ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(LoginRequest loginRequest);
}
