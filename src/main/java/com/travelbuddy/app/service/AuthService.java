package com.travelbuddy.app.service;

import com.travelbuddy.app.enums.AuthProviderEnum;
import com.travelbuddy.app.exceptions.CoreException;
import com.travelbuddy.app.model.AuthResponse;
import com.travelbuddy.app.model.RegisterInput;
import com.travelbuddy.app.model.UserEntity;
import com.travelbuddy.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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
//          below is the manual way to search for the user
//          you check if the email exists or not and then
//          you check if the raw password matches the encoded hashed password.

       /* UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CoreException(HttpStatus.BAD_REQUEST,"User not found"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new CoreException(HttpStatus.UNAUTHORIZED,"Invalid credentials");
        }*/

        //***********************************************************************************************//

        // you let Spring Security handle the authentication (throws exception if invalid)
        // it takes the email id and password and checks if the user exists
        // security config has the authProvider bean which uses the userDetailsService bean for authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // If the code reaches here, authentication was successful.
        // We know the user exists, so we fetch them to generate the token.
        UserEntity user = userRepository.findByEmail(email).orElseThrow();

        String token = jwtService.generateToken(user.getId());

        return new AuthResponse(token, user);
    }
}
