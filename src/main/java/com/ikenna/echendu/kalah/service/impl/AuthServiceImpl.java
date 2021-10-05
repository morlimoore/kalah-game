package com.ikenna.echendu.kalah.service.impl;

import com.ikenna.echendu.kalah.dto.request.LoginRequest;
import com.ikenna.echendu.kalah.dto.request.SignUpRequest;
import com.ikenna.echendu.kalah.entity.User;
import com.ikenna.echendu.kalah.exception.ApiException;
import com.ikenna.echendu.kalah.model.Enum;
import com.ikenna.echendu.kalah.payload.ApiResponse;
import com.ikenna.echendu.kalah.payload.CreateResponse;
import com.ikenna.echendu.kalah.payload.JwtResponse;
import com.ikenna.echendu.kalah.repository.UserRepository;
import com.ikenna.echendu.kalah.security.JwtUtils;
import com.ikenna.echendu.kalah.security.UserDetailsImpl;
import com.ikenna.echendu.kalah.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static com.ikenna.echendu.kalah.payload.CreateResponse.successResponse;
import static com.ikenna.echendu.kalah.util.DateTimeUtil.getDateTimeFormat;
import static org.springframework.http.HttpStatus.*;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public ResponseEntity<ApiResponse<CreateResponse.Response>> createUserAccount(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername()))
            throw new ApiException(BAD_REQUEST, "Username is already taken!");
        if (userRepository.existsByEmail(signUpRequest.getEmail()))
            throw new ApiException(BAD_REQUEST, "Email address is already taken!");

        User user = modelMapper.map(signUpRequest, User.class);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(Enum.Role.PLAYER);
        userRepository.save(user);
        return successResponse(CREATED, "User Registration Successful");
    }

    @Override
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            } catch (BadCredentialsException e) {
            throw new ApiException(BAD_REQUEST, "Invalid username or password. " +
                    "Please check your credentials and try again. " +
                    "If you don't have an account, sign up using: 'http://<host>:<port>/auth/signup");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList()).get(0);
        LocalDateTime presentDateTime = LocalDateTime.now();
        JwtResponse result = JwtResponse.builder()
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .role(role)
                .type(Enum.TokenType.BEARER.toString())
                .token(jwt)
                .issuedAt(presentDateTime.format(getDateTimeFormat()))
                .expiry(presentDateTime.plusSeconds(jwtUtils.getTokenValidityInSecs()).format(getDateTimeFormat()))
                .build();
        return successResponse(OK, result);
    }
}