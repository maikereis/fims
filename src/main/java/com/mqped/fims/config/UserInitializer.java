package com.mqped.fims.config;

import com.mqped.fims.model.entity.Role;
import com.mqped.fims.model.entity.User;
import com.mqped.fims.model.enums.RoleName;
import com.mqped.fims.repository.RoleRepository;
import com.mqped.fims.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

@Configuration
@Profile("!test")
public class UserInitializer {

    private static final Logger userInitializerLogger = LoggerFactory.getLogger(UserInitializer.class);

    @Value("${admin.user:admin}")
    private String adminUsername;

    @Value("${admin.email:admin@example.com}")
    private String adminEmail;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    @Bean
    @Order(2)
    CommandLineRunner initUsers(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if admin user already exists
            Optional<User> existingAdmin = userRepository.findByUsername(adminUsername);

            if (existingAdmin.isPresent()) {
                userInitializerLogger.info("✅ Default admin user '{}' already exists", adminUsername);
                return;
            }

            // Ensure admin role exists
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found. Did DatabaseInitializer run?"));

            // Create default admin user
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);
            userInitializerLogger.info("Created default admin user: username='{}', email='{}'", adminUsername,
                    adminEmail);
        };
    }
}
