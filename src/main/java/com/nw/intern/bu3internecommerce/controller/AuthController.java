package com.nw.intern.bu3internecommerce.controller;

import com.nw.intern.bu3internecommerce.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    public ResponseEntity<User> createUserHandler(){
        return null;
    }
}
