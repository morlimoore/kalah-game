package com.ikenna.echendu.kalah.service;

import com.ikenna.echendu.kalah.dto.LoginRequestDto;
import com.ikenna.echendu.kalah.dto.SignUpRequestDto;
import com.ikenna.echendu.kalah.payload.ApiResponse;
import com.ikenna.echendu.kalah.payload.JwtResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    Boolean ifUsernameExists(String username);

    Boolean ifEmailExists(String email);

    ResponseEntity<ApiResponse<String>> createUserAccount(SignUpRequestDto signUpRequestDTO);

    ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(LoginRequestDto loginRequestDTO);
}
