package com.mqped.fims.service;

import com.mqped.fims.exceptions.AccountDisabledException;
import com.mqped.fims.exceptions.AccountLockedException;
import com.mqped.fims.exceptions.UnauthorizedException;
import com.mqped.fims.model.entity.User;
import com.mqped.fims.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException(
                        "User not found with username: " + username));

        // Check if account is enabled
        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new AccountDisabledException(
                    "Account is disabled. Please contact support.");
        }

        // Check if account is locked
        if (Boolean.FALSE.equals(user.getAccountNonLocked())) {
            throw new AccountLockedException(
                    "Account is locked. Please contact support.");
        }

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                user.getAccountNonLocked(),
                authorities);
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException(
                        "User not found with email: " + email));

        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new AccountDisabledException(
                    "Account is disabled. Please contact support.");
        }

        if (Boolean.FALSE.equals(user.getAccountNonLocked())) {
            throw new AccountLockedException(
                    "Account is locked. Please contact support.");
        }

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                true,
                true,
                user.getAccountNonLocked(),
                authorities);
    }
}