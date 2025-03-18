package com.nw.intern.bu3internecommerce.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nw.intern.bu3internecommerce.exception.ErrorDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request
            , HttpServletResponse response
            , AuthenticationException authException)
            throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ErrorDetails errorDetails = ErrorDetails.builder()
                .path(request.getRequestURI())
                .message(authException.getMessage())
                .errorCode(HttpServletResponse.SC_UNAUTHORIZED)
                .timestamp(new Date())
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorDetails);
    }
}
