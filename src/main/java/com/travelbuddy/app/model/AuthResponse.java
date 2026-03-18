package com.travelbuddy.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;
    private String email;
    private UUID userId;
    private List<String> roles;

    public AuthResponse(String token, UserEntity user) {
        this.token = token;
        this.email = user.getEmail();
        this.userId = user.getId();
        this.roles = List.of();
    }

}