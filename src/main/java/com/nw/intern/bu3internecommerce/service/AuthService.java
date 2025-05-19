package com.nw.intern.bu3internecommerce.service;

import com.nw.intern.bu3internecommerce.dto.request.LoginRequest;
import com.nw.intern.bu3internecommerce.dto.request.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);

    String login(LoginRequest request);
}
