package com.mqped.fims.security;

import com.mqped.fims.exceptions.InvalidTokenException;
import com.mqped.fims.exceptions.TokenExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for generating, parsing, and validating JWT tokens.
 *
 * <p>
 * Uses HS256 symmetric signing algorithm with a base64-encoded secret key.
 * Provides typed exceptions for token errors.
 * </p>
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationMs;

    /**
     * Extracts the username (subject) from a valid JWT.
     *
     * @throws TokenExpiredException if the token is expired
     * @throws InvalidTokenException if malformed or invalid
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException("JWT token has expired", ex);
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new InvalidTokenException("Invalid JWT token: " + ex.getMessage(), ex);
        } catch (JwtException ex) {
            throw new InvalidTokenException("JWT parsing failed: " + ex.getMessage(), ex);
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (TokenExpiredException ex) {
            return true;
        }
    }

    /**
     * Validates a JWT token against the given user details.
     *
     * @return true if token is valid and belongs to the given user
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (TokenExpiredException ex) {
            throw new TokenExpiredException("JWT token expired", ex);
        } catch (InvalidTokenException ex) {
            throw new InvalidTokenException("JWT validation failed: " + ex.getMessage(), ex);
        }
    }

    /**
     * Generates a new JWT token for the given user with no extra claims.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }

    /**
     * Generates a new JWT token for the given user with extra claims.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(expirationMs);

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Returns the signing key derived from the base64-encoded secret.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Checks if a JWT has a valid structure and signature.
     * Useful for early rejection before deeper validation.
     */
    public boolean validateTokenStructure(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
}
