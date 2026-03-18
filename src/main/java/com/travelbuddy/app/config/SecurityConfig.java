package com.travelbuddy.app.config;

import com.travelbuddy.app.filter.JwtAuthFilter;
import com.travelbuddy.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final UserRepository userRepository;
    private final JwtAuthFilter jwtAuthFilter;
    
    // 1. Tell Spring how to find users
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // 2. Configure the Authentication Provider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // In Spring Security 7+, DaoAuthenticationProvider requires the UserDetailsService in its constructor
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // 3. Expose the AuthenticationManager bean so you can use it in AuthService which
    // does the authentication from the email and password provided
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // provide BCrypt encoder here, default strength is 10
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth ->
                auth
                .requestMatchers(
                        "/api/v1/health",
                        "/api/v1/register",
                        "/api/v1/login")
                .permitAll() // Allow unauthenticated access
                .anyRequest().authenticated() // All other requests require authentication
            )
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for REST APIs
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .authenticationProvider(authenticationProvider()) // Add the custom provider
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
