package com.ikenna.echendu.kalah.controller;

import com.ikenna.echendu.kalah.dto.LoginRequestDto;
import com.ikenna.echendu.kalah.dto.SignUpRequestDto;
import com.ikenna.echendu.kalah.payload.ApiResponse;
import com.ikenna.echendu.kalah.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.ikenna.echendu.kalah.payload.CreateResponse.errorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> registerUser(@Valid @RequestBody SignUpRequestDto signUpRequestDTO, BindingResult result) {
        if (result.hasErrors())
            return errorResponse(result.getFieldError().getDefaultMessage(), BAD_REQUEST);

        else if (authService.ifUsernameExists(signUpRequestDTO.getUsername()))
            return errorResponse("Username is already taken!", BAD_REQUEST);
        else if (authService.ifEmailExists(signUpRequestDTO.getEmail()))
            return errorResponse("Email address is already taken!", BAD_REQUEST);

        return authService.createUserAccount(signUpRequestDTO);
    }

    @PostMapping("/login")
    public HttpEntity<?> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDTO,
                                       BindingResult result) {
        if (result.hasErrors())
            return errorResponse(result.getFieldError().getDefaultMessage(), BAD_REQUEST);
        return authService.authenticateUser(loginRequestDTO);
    }
}
