package com.mqped.fims.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Custom AuthenticationEntryPoint that handles unauthorized access attempts
 * by returning a standardized JSON response instead of a default HTML error
 * page.
 *
 * <p>
 * Useful for stateless REST APIs using JWT authentication.
 * </p>
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        logger.warn("Unauthorized access attempt at '{}': {}",
                request.getServletPath(),
                authException.getMessage());

        // Prepare standard JSON error response
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", resolveErrorMessage(authException));
        body.put("path", request.getServletPath());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        try {
            objectMapper.writeValue(response.getOutputStream(), body);
        } catch (IOException ex) {
            logger.error("Failed to write unauthorized response body", ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    /**
     * Determines the most appropriate error message based on the exception type.
     * This can be expanded for JWT expiration, malformed tokens, etc.
     */
    private String resolveErrorMessage(AuthenticationException ex) {
        String msg = ex.getMessage();
        if (msg == null || msg.isBlank()) {
            return "Full authentication is required to access this resource";
        }
        // Optionally sanitize to avoid leaking internal details
        if (msg.toLowerCase().contains("jwt")) {
            return "Invalid or expired authentication token";
        }
        return msg;
    }
}
