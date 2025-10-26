package com.example.backend.Security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.backend.Model.User.User;
import com.example.backend.Repositories.UserRepository;


@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminUsername = "admin";
            String envPassword = System.getenv("ADMIN_PASSWORD");

            if (envPassword == null || envPassword.isBlank()) {
                System.err.println("ADMIN_PASSWORD not set. Skipping admin creation.");
                return;
            }

            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode(envPassword));
                admin.setRole(User.Roles.Admin); 
                userRepository.save(admin);
                System.out.println("Admin user created: username=" + adminUsername);
            } else {
                System.out.println("ℹAdmin user already exists — skipping creation.");
            }
        };
    }
}