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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
@Profile("!test")
public class UserInitializer {

    private static final Logger userInitializerLogger = LoggerFactory.getLogger(UserInitializer.class);

    // Inject values from application-dev.properties or .env
    @Value("${admin.user}")
    private String adminUsername;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    @Order(2)
    CommandLineRunner initUsers(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setEmail(adminEmail);
                admin.setPassword(new BCryptPasswordEncoder().encode(adminPassword));
                admin.setRoles(Set.of(adminRole));

                userRepository.save(admin);
                userInitializerLogger.info("Created default admin user: {} / {}", adminUsername, adminPassword);
            } else {
                userInitializerLogger.info("Default admin user '{}' already exists", adminUsername);
            }
        };
    }
}
