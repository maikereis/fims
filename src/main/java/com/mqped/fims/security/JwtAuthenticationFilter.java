package com.mqped.fims.security;

import io.jsonwebtoken.ExpiredJwtException;
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

/**
 * Security filter responsible for extracting and validating JWT tokens
 * from incoming HTTP requests. If valid, it populates the Spring Security
 * context with the corresponding authenticated user.
 *
 * <p>
 * This filter runs once per request, before the controller layer.
 * </p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger authLogger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

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

            if (jwt != null) {
                String username = jwtUtil.extractUsername(jwt);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    authenticateUser(jwt, username, request);
                }
            }

        } catch (ExpiredJwtException ex) {
            authLogger.warn("JWT expired for user '{}': {}", ex.getClaims().getSubject(), ex.getMessage());
        } catch (JwtException ex) {
            authLogger.warn("Invalid JWT token: {}", ex.getMessage());
        } catch (Exception ex) {
            authLogger.error("Error during JWT authentication: {}", ex.getMessage(), ex);
        }

        // Continue the filter chain even if JWT validation fails
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header if present and valid.
     *
     * @param request the HTTP request
     * @return the extracted JWT token or null if none
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX_LENGTH).trim();
        }

        return null;
    }

    /**
     * Authenticates the user based on the JWT token and sets the authentication
     * context for the current request.
     *
     * @param jwt      the raw JWT token
     * @param username the username extracted from the token
     * @param request  the current HTTP request
     */
    private void authenticateUser(String jwt, String username, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            authLogger.debug("User '{}' successfully authenticated from JWT", username);
        } else {
            authLogger.debug("Invalid JWT for user '{}'", username);
        }
    }
}
