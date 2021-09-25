package com.ikenna.echendu.kalah.security;

import com.ikenna.echendu.kalah.payload.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ikenna.echendu.kalah.util.AppUtil.getErrorMessage;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@Slf4j
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error("Authorization error: {}", getErrorMessage());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ApiResponse<String> filterResponse = new ApiResponse<>();
        filterResponse.setStatus(UNAUTHORIZED);
        filterResponse.setMessage("ERROR");
        filterResponse.setResult(getErrorMessage());
        response.getOutputStream().println(filterResponse.toString());
    }
}