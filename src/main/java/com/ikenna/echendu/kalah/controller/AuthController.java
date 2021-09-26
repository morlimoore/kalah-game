package com.ikenna.echendu.kalah.controller;

import com.ikenna.echendu.kalah.dto.request.LoginRequest;
import com.ikenna.echendu.kalah.dto.request.SignUpRequest;
import com.ikenna.echendu.kalah.exception.ApiException;
import com.ikenna.echendu.kalah.payload.ApiResponse;
import com.ikenna.echendu.kalah.payload.CreateResponse;
import com.ikenna.echendu.kalah.payload.JwtResponse;
import com.ikenna.echendu.kalah.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<CreateResponse.Success>> registerUser(@Valid @RequestBody SignUpRequest signUpRequest,
                                                                            BindingResult result) {
        if (result.hasErrors())
            throw new ApiException(BAD_REQUEST, result.getFieldError().getDefaultMessage());

        return authService.createUserAccount(signUpRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest,
                                                              BindingResult result) {
        if (result.hasErrors())
            throw new ApiException(BAD_REQUEST, result.getFieldError().getDefaultMessage());
        return authService.authenticateUser(loginRequest);
    }
}
