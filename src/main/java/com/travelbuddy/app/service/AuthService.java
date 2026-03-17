package com.travelbuddy.app.service;

import com.travelbuddy.app.enums.AuthProviderEnum;
import com.travelbuddy.app.exceptions.CoreException;
import com.travelbuddy.app.model.AuthResponse;
import com.travelbuddy.app.model.RegisterInput;
import com.travelbuddy.app.model.UserEntity;
import com.travelbuddy.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterInput registerInput) {

        if (userRepository.existsByEmail(registerInput.getEmail())) {
            throw new CoreException(HttpStatus.BAD_REQUEST, "User with this email already exists");
        }

        UserEntity newUser = UserEntity.builder()
                .firstname(registerInput.getFirstname())
                .lastname(registerInput.getLastname())
                .email(registerInput.getEmail())
                .passwordHash(passwordEncoder.encode(registerInput.getPassword()))
                .provider(AuthProviderEnum.LOCAL)
                .createdAt(Instant.now())
                .build();

        userRepository.save(newUser);

        String token = jwtService.generateToken(newUser.getId());

        return new AuthResponse(token, newUser);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(String email, String password) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CoreException(HttpStatus.BAD_REQUEST,"User not found"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new CoreException(HttpStatus.UNAUTHORIZED,"Invalid credentials");
        }

        String token = jwtService.generateToken(user.getId());

        return new AuthResponse(token, user);
    }
}
