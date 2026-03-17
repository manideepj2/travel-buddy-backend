package com.travelbuddy.app.controllers;

import com.travelbuddy.app.model.AuthResponse;
import com.travelbuddy.app.model.LoginInput;
import com.travelbuddy.app.model.RegisterInput;
import com.travelbuddy.app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/health")
    public String hello() {
        return "TravelBuddy API running";
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterInput input) {

        return ResponseEntity.ok(authService.register(input));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginInput input) {

        return ResponseEntity.ok(authService.login(input.getEmail(), input.getPassword()));
    }
}