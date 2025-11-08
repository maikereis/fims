package com.mqped.fims.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger authLogger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = 7;

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Add @Lazy annotation to break circular dependency
    public JwtAuthenticationFilter(JwtUtil jwtUtil, @Lazy UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = extractJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                String username = jwtUtil.extractUsername(jwt);

                if (StringUtils.hasText(username) &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    authenticateUser(jwt, username, request);
                }
            }
        } catch (JwtException ex) {
            authLogger.error("JWT token validation failed: {}", ex.getMessage());
        } catch (Exception ex) {
            authLogger.error("Cannot set user authentication: {}", ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract JWT token from Authorization header
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX_LENGTH);
        }

        return null;
    }

    /**
     * Authenticate user with JWT token
     */
    private void authenticateUser(String jwt, String username, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            authLogger.debug("User '{}' authenticated successfully", username);
        }
    }
}