package com.travelbuddy.app.service;

import com.travelbuddy.app.enums.AuthProviderEnum;
import com.travelbuddy.app.model.AuthResponse;
import com.travelbuddy.app.model.RegisterInput;
import com.travelbuddy.app.model.UserEntity;
import com.travelbuddy.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterInput registerInput) {

        UserEntity user = UserEntity.builder()
                .firstname(registerInput.getFirstname())
                .lastname(registerInput.getLastname())
                .email(registerInput.getEmail())
                .passwordHash(passwordEncoder.encode(registerInput.getPassword()))
                .provider(AuthProviderEnum.LOCAL)
                .createdAt(Instant.now())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getId());

        return new AuthResponse(token, user);
    }

    public AuthResponse login(String email, String password) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getId());

        return new AuthResponse(token, user);
    }
}
