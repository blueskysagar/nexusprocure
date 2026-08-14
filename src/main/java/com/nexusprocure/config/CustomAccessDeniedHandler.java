package com.nexusprocure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexusprocure.exception.ErrorResponse;
import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import jakarta.servlet.ServletException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    public CustomAccessDeniedHandler(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)throws IOException, ServletException {

     response.setStatus(HttpServletResponse.SC_FORBIDDEN);
     response.setContentType("application/json");
        ErrorResponse errorResponse = new ErrorResponse(
                "Access Denied",
                 403,
                 LocalDateTime.now(),
                 request.getRequestURI(),
                  "Forbidden");
        objectMapper.writeValue(response.getOutputStream(),errorResponse);


    }


}
