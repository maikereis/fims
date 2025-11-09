package com.mqped.fims.config;

import com.mqped.fims.model.entity.Role;
import com.mqped.fims.model.enums.RoleName;
import com.mqped.fims.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

@Configuration
@Profile("!test")
public class DatabaseInitializer {

    private static final Logger databaseInitializerLogger = LoggerFactory.getLogger(DatabaseInitializer.class);

    /**
     * Initialize default roles in the database
     */
    @Bean
    @Order(1)
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (Boolean.FALSE.equals(roleRepository.existsByName(RoleName.ROLE_USER))) {
                Role userRole = new Role(RoleName.ROLE_USER);
                roleRepository.save(userRole);
                databaseInitializerLogger.info("Created ROLE_USER");
            }

            if (Boolean.FALSE.equals(roleRepository.existsByName(RoleName.ROLE_ADMIN))) {
                Role adminRole = new Role(RoleName.ROLE_ADMIN);
                roleRepository.save(adminRole);
                databaseInitializerLogger.info("Created ROLE_ADMIN");
            }

            if (Boolean.FALSE.equals(roleRepository.existsByName(RoleName.ROLE_MODERATOR))) {
                Role modRole = new Role(RoleName.ROLE_MODERATOR);
                roleRepository.save(modRole);
                databaseInitializerLogger.info("Created ROLE_MODERATOR");
            }

            databaseInitializerLogger.info("Database initialization complete!");
        };
    }
}
