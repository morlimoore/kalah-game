package com.ikenna.echendu.kalah.service.impl;

import com.ikenna.echendu.kalah.dto.LoginRequestDto;
import com.ikenna.echendu.kalah.dto.SignUpRequestDto;
import com.ikenna.echendu.kalah.entity.User;
import com.ikenna.echendu.kalah.model.Enum;
import com.ikenna.echendu.kalah.payload.ApiResponse;
import com.ikenna.echendu.kalah.payload.JwtResponse;
import com.ikenna.echendu.kalah.repository.UserRepository;
import com.ikenna.echendu.kalah.security.JwtUtils;
import com.ikenna.echendu.kalah.security.UserDetailsImpl;
import com.ikenna.echendu.kalah.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static com.ikenna.echendu.kalah.payload.CreateResponse.successResponse;
import static com.ikenna.echendu.kalah.util.AppUtil.getDateTimeFormat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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
    public Boolean ifUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean ifEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public ResponseEntity<ApiResponse<String>> createUserAccount(SignUpRequestDto signUpRequestDTO) {
        User user = modelMapper.map(signUpRequestDTO, User.class);
        user.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
        user.setRole(Enum.Role.PLAYER);
        userRepository.save(user);
        return successResponse("User Registration Successful", CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(LoginRequestDto loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

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
        return successResponse(result, OK);
    }
}