package com.nexusprocure.common.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {
    private static final String CORRELATION_ID_HEADER = "x-CorrelationId";
    private static final String CORRELATION_ID_MDC_KEY = "correlationId";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if(correlationId == null || correlationId.isBlank()){
            correlationId = UUID.randomUUID().toString();

        }
        try{
            MDC.put(CORRELATION_ID_MDC_KEY, correlationId);
            response.setHeader("x-Correlation-Id", correlationId);
            filterChain.doFilter(request,response);
        } finally {
            MDC.clear();

    }

    }
}
