package com.mqped.fims.controller;

import com.mqped.fims.exceptions.UnauthorizedException;
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

/**
 * REST controller responsible for authentication and authorization operations.
 * <p>
 * Provides endpoints for user login, registration, logout, and fetching the
 * currently authenticated user.
 * It uses JWT (JSON Web Token) for stateless authentication and integrates with
 * Spring Security.
 * </p>
 *
 * <h2>Available Endpoints</h2>
 * <ul>
 * <li><b>POST /api/auth/login</b> — Authenticate a user and return a JWT
 * token.</li>
 * <li><b>POST /api/auth/signup</b> — Register a new user with optional role
 * assignment.</li>
 * <li><b>POST /api/auth/logout</b> — Log out the current user (client-side
 * token invalidation).</li>
 * <li><b>GET /api/auth/me</b> — Retrieve details of the currently authenticated
 * user.</li>
 * </ul>
 *
 * @author Rodrigo
 * @since 1.0
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;

        /**
         * Constructs a new {@code AuthController} with the required authentication and
         * persistence components.
         *
         * @param authenticationManager the Spring Security authentication manager.
         * @param userRepository        the repository for {@link User} persistence
         *                              operations.
         * @param roleRepository        the repository for {@link Role} management.
         * @param passwordEncoder       the encoder used to securely hash user
         *                              passwords.
         * @param jwtUtil               the utility class for generating and validating
         *                              JWT tokens.
         */
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
         * Authenticates a user using their username and password credentials.
         * <p>
         * Upon successful authentication, this method generates and returns a JWT token
         * along with user details and assigned roles.
         * </p>
         *
         * @param loginRequest the {@link LoginRequestDTO} containing username and
         *                     password.
         * @return a {@link ResponseEntity} containing a {@link JwtResponseDTO} with the
         *         JWT token and user information.
         * @throws org.springframework.security.core.AuthenticationException if the
         *                                                                   authentication
         *                                                                   fails.
         */
        @PostMapping("/login")
        public ResponseEntity<JwtResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                loginRequest.getUsername(),
                                                loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String jwt = jwtUtil.generateToken(userDetails);

                Set<String> roles = userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toSet());

                User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);

                return ResponseEntity.ok(new JwtResponseDTO(
                                jwt,
                                user.getId(),
                                user.getUsername(),
                                user.getEmail(),
                                roles));
        }

        /**
         * Registers a new user in the system.
         * <p>
         * Validates username and email uniqueness, encodes the password,
         * and assigns default or provided roles.
         * </p>
         *
         * @param signupRequest the {@link SignupRequestDTO} containing registration
         *                      details.
         * @return a {@link ResponseEntity} with a {@link MessageResponseDTO} indicating
         *         success or failure.
         */
        @PostMapping("/signup")
        public ResponseEntity<MessageResponseDTO> registerUser(@Valid @RequestBody SignupRequestDTO signupRequest) {
                if (Boolean.TRUE.equals(userRepository.existsByUsername(signupRequest.getUsername()))) {
                        return ResponseEntity
                                        .badRequest()
                                        .body(new MessageResponseDTO("Error: Username is already taken!"));
                }

                if (Boolean.TRUE.equals(userRepository.existsByEmail(signupRequest.getEmail()))) {
                        return ResponseEntity
                                        .badRequest()
                                        .body(new MessageResponseDTO("Error: Email is already in use!"));
                }

                User user = new User(
                                signupRequest.getUsername(),
                                signupRequest.getEmail(),
                                passwordEncoder.encode(signupRequest.getPassword()));

                Set<String> strRoles = signupRequest.getRoles();
                Set<Role> roles = new HashSet<>();

                if (strRoles == null || strRoles.isEmpty()) {
                        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                                        .orElseThrow(() -> new RuntimeException("Error: Role USER not found."));
                        roles.add(userRole);
                } else {
                        strRoles.forEach(role -> {
                                switch (role.toUpperCase()) {
                                        case "ADMIN" -> roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Error: Role ADMIN not found.")));
                                        case "MODERATOR" -> roles.add(roleRepository.findByName(RoleName.ROLE_MODERATOR)
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Error: Role MODERATOR not found.")));
                                        default -> roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Error: Role USER not found.")));
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
         * Logs out the currently authenticated user.
         * <p>
         * Since JWT authentication is stateless, this method simply clears the
         * {@link SecurityContextHolder}, effectively removing authentication data
         * from the current session.
         * </p>
         *
         * @return a {@link ResponseEntity} with a {@link MessageResponseDTO} confirming
         *         the logout.
         */
        @PostMapping("/logout")
        public ResponseEntity<MessageResponseDTO> logoutUser() {
                SecurityContextHolder.clearContext();
                return ResponseEntity.ok(new MessageResponseDTO("User logged out successfully!"));
        }

        /**
         * Retrieves information about the currently authenticated user.
         *
         * @param authentication the Spring Security {@link Authentication} object
         *                       automatically provided.
         * @return a {@link ResponseEntity} containing the {@link JwtResponseDTO} with
         *         user information.
         * @throws UnauthorizedException if no user is authenticated or the
         *                               authentication is invalid.
         */
        @GetMapping("/me")
        public ResponseEntity<JwtResponseDTO> getCurrentUser(Authentication authentication) {
                if (authentication == null || !authentication.isAuthenticated()) {
                        throw new UnauthorizedException("You must be authenticated to access this resource");
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
