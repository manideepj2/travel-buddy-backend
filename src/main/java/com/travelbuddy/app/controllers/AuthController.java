package com.travelbuddy.app.controllers;

import com.travelbuddy.app.model.AuthResponse;
import com.travelbuddy.app.model.RegisterInput;
import com.travelbuddy.app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @MutationMapping
    public AuthResponse register(
            @Argument RegisterInput input
    ) {

        return authService.register(input);
    }

    @MutationMapping
    public AuthResponse login(
            @Argument String email,
            @Argument String password
    ) {

        return authService.login(email, password);
    }
}