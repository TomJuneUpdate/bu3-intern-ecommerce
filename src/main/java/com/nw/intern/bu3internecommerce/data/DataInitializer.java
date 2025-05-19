package com.nw.intern.bu3internecommerce.data;


import com.nw.intern.bu3internecommerce.entity.user.Role;
import com.nw.intern.bu3internecommerce.entity.user.User;
import com.nw.intern.bu3internecommerce.repository.RoleRepository;
import com.nw.intern.bu3internecommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_USER" , "ROLE_ADMIN", "ROLE_CUSTOMER");
        createDefaultRoles(defaultRoles);
        createDefaultAdminIfNotExits();
    }

    private void createDefaultRoles(Set<String> roles){
        roles.stream()
                .filter(role -> Optional.ofNullable(roleRepository.findByName(role))
                        .isEmpty()).map(Role::new).forEach(roleRepository::save);
    }

    private void createDefaultAdminIfNotExits(){
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("Role not found!"));
        for (int i = 1; i<=3; i++){
            String defaultEmail = "admin"+i+"@email.com";
            if (userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Shop User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            user.setCreatedBy("system"); // ✅ Gán giá trị vào đây
            user.setUpdatedBy("system");
            userRepository.save(user);
        }
    }

}
