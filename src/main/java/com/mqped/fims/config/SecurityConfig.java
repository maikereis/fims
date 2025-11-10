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
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                // Stateless + token-based, so disable CSRF
                                .csrf(AbstractHttpConfigurer::disable)

                                // Enable CORS using your configuration bean
                                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))

                                // Unauthorized / entry point handling
                                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))

                                // Stateless sessions (JWT carries identity)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                // Define authorization rules
                                .authorizeHttpRequests(auth -> auth

                                                // Public authentication endpoints
                                                .requestMatchers("/api/auth/**").permitAll()

                                                // Public health checks
                                                .requestMatchers(HttpMethod.GET, "/api/*/check").permitAll()

                                                // Allow H2 console (use only in dev)
                                                .requestMatchers("/h2-console/**").permitAll()

                                                // Static resources
                                                .requestMatchers(
                                                                "/",
                                                                "/favicon.ico",
                                                                "/images/**",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/webjars/**")
                                                .permitAll()

                                                // OpenAPI/Swagger
                                                .requestMatchers(
                                                                "/swagger-ui/**",
                                                                "/swagger-ui.html",
                                                                "/v3/api-docs/**",
                                                                "/swagger-resources/**",
                                                                "/webjars/**")
                                                .permitAll()

                                                // Actuator endpoints
                                                .requestMatchers(
                                                                "/actuator/health",
                                                                "/actuator/info",
                                                                "/actuator/prometheus")
                                                .permitAll()
                                                .requestMatchers("/actuator/**").hasRole("ADMIN")

                                                // All other routes require auth
                                                .anyRequest().authenticated())

                                // Authentication Provider
                                .authenticationProvider(authenticationConfig.authenticationProvider())

                                // Add JWT filter before UsernamePasswordAuthenticationFilter
                                .addFilterBefore(jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class)

                                // Allow H2 console frames (only for dev!)
                                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

                return http.build();
        }
}
