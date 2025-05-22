package com.nw.intern.bu3internecommerce.service.impl;

import com.nw.intern.bu3internecommerce.config.jwt.JwtUtils;
import com.nw.intern.bu3internecommerce.dto.request.LoginRequest;
import com.nw.intern.bu3internecommerce.dto.request.RegisterRequest;
import com.nw.intern.bu3internecommerce.entity.cart.Cart;
import com.nw.intern.bu3internecommerce.entity.user.Role;
import com.nw.intern.bu3internecommerce.entity.user.User;
import com.nw.intern.bu3internecommerce.repository.CartRepository;
import com.nw.intern.bu3internecommerce.repository.RoleRepository;
import com.nw.intern.bu3internecommerce.repository.UserRepository;
import com.nw.intern.bu3internecommerce.service.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CartRepository cartRepository;
    private final RoleRepository roleRepository;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được dùng!");
        }
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("Role not found!"));
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setRoles(List.of(userRole));
        user.setActive(true);
        userRepository.save(user);
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

    @Override
    public String login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        return jwtUtils.generateAccessTokenForUser(authentication);
    }
}
