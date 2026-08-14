package com.nexusprocure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexusprocure.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import org.springframework.security.core.AuthenticationException;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }
    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)throws IOException

    {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ErrorResponse error = new ErrorResponse("Authentication required", HttpServletResponse.SC_UNAUTHORIZED, LocalDateTime.now(), request.getRequestURI(),"Unauthorized");
        objectMapper.writeValue(response.getOutputStream(), error);

    }
}
