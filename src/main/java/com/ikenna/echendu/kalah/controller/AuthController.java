package com.ikenna.echendu.kalah.controller;

import com.ikenna.echendu.kalah.dto.request.LoginRequest;
import com.ikenna.echendu.kalah.dto.request.SignUpRequest;
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

import static com.ikenna.echendu.kalah.util.ValidationUtil.checkFieldErrors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<CreateResponse.Response>> registerUser(@Valid @RequestBody SignUpRequest signUpRequest,
                                                                             BindingResult result) {
        checkFieldErrors(result);
        return authService.createUserAccount(signUpRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest,
                                                              BindingResult result) {
        checkFieldErrors(result);
        return authService.authenticateUser(loginRequest);
    }
}
