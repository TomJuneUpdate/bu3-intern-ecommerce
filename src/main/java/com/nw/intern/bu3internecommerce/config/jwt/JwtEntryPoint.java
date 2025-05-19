package com.nw.intern.bu3internecommerce.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nw.intern.bu3internecommerce.dto.response.ApiResponse;
import com.nw.intern.bu3internecommerce.exception.AccountNotActive;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String message;

        Throwable rootCause = authException.getCause(); // 💡 quan trọng

        if (authException instanceof BadCredentialsException) {
            message = "Sai email hoặc mật khẩu!";
        } else if (rootCause instanceof AccountNotActive) {
            message = rootCause.getMessage(); // sẽ lấy được "Tài khoản chưa được kích hoạt"
        } else {
            message = "Lỗi xác thực. Vui lòng thử lại sau!";
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(ApiResponse.fail(message)));
    }


}
