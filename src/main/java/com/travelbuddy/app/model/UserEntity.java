package com.travelbuddy.app.model;

import com.travelbuddy.app.enums.AuthProviderEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity implements UserDetails {
    /*should implement UserDetails so spring knows this is the class
     that it can use to find the user while authenticating*/
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private AuthProviderEnum provider;

    private String providerId;

    private Instant createdAt;

    private Instant updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // Return roles/authorities here
    }

    @Override
    public String getPassword() {
        return passwordHash; // Tell Spring which field holds the password
    }

    @Override
    public String getUsername() {
        return email; // We use email as the username
    }
}
