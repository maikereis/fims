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
import org.springframework.transaction.annotation.Transactional;

/**
 * Initializes essential database data such as default roles.
 * <p>
 * Runs only when the "test" profile is NOT active.
 */
@Configuration
@Profile("!test")
public class DatabaseInitializer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Bean
    @Order(1)
    @Transactional
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            createRoleIfNotExists(roleRepository, RoleName.ROLE_USER);
            createRoleIfNotExists(roleRepository, RoleName.ROLE_ADMIN);
            createRoleIfNotExists(roleRepository, RoleName.ROLE_MODERATOR);

            log.info("✅ Database initialization complete.");
        };
    }

    private void createRoleIfNotExists(RoleRepository roleRepository, RoleName roleName) {
        if (Boolean.FALSE.equals(roleRepository.existsByName(roleName))) {
            roleRepository.save(new Role(roleName));
            log.info("Created role: {}", roleName);
        }
    }
}
