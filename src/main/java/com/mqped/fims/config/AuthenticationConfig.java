package com.mqped.fims.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configures authentication components:
 * <ul>
 * <li>Custom {@link AuthenticationProvider} for username/password
 * validation</li>
 * <li>{@link AuthenticationManager} bean</li>
 * <li>BCrypt password encoder</li>
 * </ul>
 *
 * <p>
 * This provider uses {@link UserDetailsService} to fetch users and
 * {@link BCryptPasswordEncoder}
 * for password verification.
 * </p>
 */
@Configuration
public class AuthenticationConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Provides a custom authentication provider that validates user credentials
     * using the
     * injected {@link UserDetailsService}.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication)
                    throws AuthenticationException {

                String username = authentication.getName();
                String rawPassword = String.valueOf(authentication.getCredentials());

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (!passwordEncoder.matches(rawPassword, userDetails.getPassword())) {
                    throw new BadCredentialsException("Invalid username or password");
                }

                return new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // credentials are not stored after successful auth
                        userDetails.getAuthorities());
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
        };
    }

    /**
     * Provides the {@link AuthenticationManager} bean, required for authentication
     * endpoints.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Provides a reusable password encoder bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }
}
