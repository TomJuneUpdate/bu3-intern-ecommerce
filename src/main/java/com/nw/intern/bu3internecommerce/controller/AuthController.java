package com.nw.intern.bu3internecommerce.controller;

import com.nw.intern.bu3internecommerce.dto.request.LoginRequest;
import com.nw.intern.bu3internecommerce.dto.request.RegisterRequest;
import com.nw.intern.bu3internecommerce.dto.response.ApiResponse;
import com.nw.intern.bu3internecommerce.entity.user.User;
import com.nw.intern.bu3internecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ApiResponse.ok();
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ApiResponse.ok(token);
    }
}
