package com.nw.intern.bu3internecommerce.config.security;

import com.nw.intern.bu3internecommerce.entity.user.User;
import com.nw.intern.bu3internecommerce.exception.AccountNotActive;
import com.nw.intern.bu3internecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại"));

        if (!user.isActive()) {
            throw new AccountNotActive("Tài khoản chưa được kích hoạt");
        }

        return ShopUserDetails.buildUserDetails(user);
    }
}
