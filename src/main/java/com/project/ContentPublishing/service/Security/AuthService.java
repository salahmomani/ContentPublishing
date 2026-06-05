package com.project.ContentPublishing.service.Security;

import com.project.ContentPublishing.Exception.ResourceNotFoundException;
import com.project.ContentPublishing.dto.Request.LoginRequest;
import com.project.ContentPublishing.dto.Request.RegisterRequest;
import com.project.ContentPublishing.dto.Response.AuthResponse;
import com.project.ContentPublishing.model.Roles;
import com.project.ContentPublishing.model.User;
import com.project.ContentPublishing.repository.UserRepository;
import com.project.ContentPublishing.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already registered");
        }

        User user = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Roles.Reader)
                .enabled(true)
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}