package com.mqped.fims.config;

import com.mqped.fims.security.JwtAuthenticationEntryPoint;
import com.mqped.fims.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfig corsConfig;
    private final AuthenticationConfig authenticationConfig;

    public SecurityConfig(
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CorsConfig corsConfig,
            AuthenticationConfig authenticationConfig) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.corsConfig = corsConfig;
        this.authenticationConfig = authenticationConfig;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF Configuration - disabled for JWT
                .csrf(AbstractHttpConfigurer::disable)

                // CORS Configuration
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))

                // Exception Handling
                .exceptionHandling(exception -> 
                    exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))

                // Session Management - Stateless for JWT
                .sessionManagement(session -> 
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Authorization Rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - authentication
                        .requestMatchers("/api/auth/**").permitAll()

                        // Public endpoints - health checks
                        .requestMatchers(HttpMethod.GET, "/api/*/check").permitAll()

                        // Swagger/OpenAPI endpoints
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**")
                        .permitAll()

                        // Actuator endpoints
                        .requestMatchers("/actuator/health", "/actuator/info", "/actuator/prometheus").permitAll()
                        .requestMatchers("/actuator/**").hasRole("ADMIN")

                        // All other endpoints require authentication
                        .anyRequest().authenticated())

                // Authentication Provider
                .authenticationProvider(authenticationConfig.authenticationProvider())

                // JWT Filter
                .addFilterBefore(jwtAuthenticationFilter, 
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}