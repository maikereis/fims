package com.mqped.fims.controller;

import com.mqped.fims.model.dto.JwtResponseDTO;
import com.mqped.fims.model.dto.LoginRequestDTO;
import com.mqped.fims.model.dto.MessageResponseDTO;
import com.mqped.fims.model.dto.SignupRequestDTO;
import com.mqped.fims.model.entity.Role;
import com.mqped.fims.model.enums.RoleName;
import com.mqped.fims.model.entity.User;
import com.mqped.fims.repository.RoleRepository;
import com.mqped.fims.repository.UserRepository;
import com.mqped.fims.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;

        public AuthController(
                        AuthenticationManager authenticationManager,
                        UserRepository userRepository,
                        RoleRepository roleRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil) {
                this.authenticationManager = authenticationManager;
                this.userRepository = userRepository;
                this.roleRepository = roleRepository;
                this.passwordEncoder = passwordEncoder;
                this.jwtUtil = jwtUtil;
        }

        /**
         * Login endpoint
         * POST /api/auth/login
         */
        @PostMapping("/login")
        public ResponseEntity<JwtResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {

                // Authenticate user
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                loginRequest.getUsername(),
                                                loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Get UserDetails
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                // Generate JWT token
                String jwt = jwtUtil.generateToken(userDetails);

                // Get user roles
                Set<String> roles = userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toSet());

                // Update last login
                User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);

                // Return JWT response
                return ResponseEntity.ok(new JwtResponseDTO(
                                jwt,
                                user.getId(),
                                user.getUsername(),
                                user.getEmail(),
                                roles));
        }

        /**
         * Signup endpoint
         * POST /api/auth/signup
         */
        @PostMapping("/signup")
        public ResponseEntity<MessageResponseDTO> registerUser(@Valid @RequestBody SignupRequestDTO signupRequest) {

                // Check if username already exists
                if (Boolean.TRUE.equals(userRepository.existsByUsername(signupRequest.getUsername()))) {
                        return ResponseEntity
                                        .badRequest()
                                        .body(new MessageResponseDTO("Error: Username is already taken!"));
                }

                // Check if email already exists
                if (Boolean.TRUE.equals(userRepository.existsByEmail(signupRequest.getEmail()))) {
                        return ResponseEntity
                                        .badRequest()
                                        .body(new MessageResponseDTO("Error: Email is already in use!"));
                }

                // Create new user
                User user = new User(
                                signupRequest.getUsername(),
                                signupRequest.getEmail(),
                                passwordEncoder.encode(signupRequest.getPassword()));

                // Assign roles
                Set<String> strRoles = signupRequest.getRoles();
                Set<Role> roles = new HashSet<>();

                if (strRoles == null || strRoles.isEmpty()) {
                        // Default role: USER
                        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                                        .orElseThrow(() -> new RuntimeException("Error: Role USER not found."));
                        roles.add(userRole);
                } else {
                        strRoles.forEach(role -> {
                                switch (role.toUpperCase()) {
                                        case "ADMIN":
                                                Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                                                                .orElseThrow(() -> new RuntimeException(
                                                                                "Error: Role ADMIN not found."));
                                                roles.add(adminRole);
                                                break;
                                        case "MODERATOR":
                                                Role modRole = roleRepository.findByName(RoleName.ROLE_MODERATOR)
                                                                .orElseThrow(() -> new RuntimeException(
                                                                                "Error: Role MODERATOR not found."));
                                                roles.add(modRole);
                                                break;
                                        default:
                                                Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                                                                .orElseThrow(() -> new RuntimeException(
                                                                                "Error: Role USER not found."));
                                                roles.add(userRole);
                                }
                        });
                }

                user.setRoles(roles);
                userRepository.save(user);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(new MessageResponseDTO("User registered successfully!"));
        }

        /**
         * Logout endpoint (optional - JWT is stateless)
         * POST /api/auth/logout
         */
        @PostMapping("/logout")
        public ResponseEntity<MessageResponseDTO> logoutUser() {
                SecurityContextHolder.clearContext();
                return ResponseEntity.ok(new MessageResponseDTO("User logged out successfully!"));
        }

        /**
         * Get current user info
         * GET /api/auth/me
         */
        @GetMapping("/me")
        public ResponseEntity<JwtResponseDTO> getCurrentUser(Authentication authentication) {
                if (authentication == null || !authentication.isAuthenticated()) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body(new JwtResponseDTO(null, null, null, null, Set.of()));
                }

                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                User user = userRepository.findByUsername(userDetails.getUsername())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Set<String> roles = user.getRoles().stream()
                                .map(role -> role.getName().name())
                                .collect(Collectors.toSet());

                return ResponseEntity.ok(new JwtResponseDTO(
                                null, // No token needed for this endpoint
                                user.getId(),
                                user.getUsername(),
                                user.getEmail(),
                                roles));
        }
}