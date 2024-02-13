package org.youcode.securitydemo2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.youcode.securitydemo2.domain.entity.Token;
import org.youcode.securitydemo2.domain.entity.User;
import org.youcode.securitydemo2.dto.AuthenticationResponse;
import org.youcode.securitydemo2.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);

        Token refreshToken = tokenService.generateRefreshToken(saved);

        String token = tokenService.generateToken(saved, refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken.getToken())
                .tokenExpiration(tokenService.extractExpiration(token))
                .build();
    }

    public AuthenticationResponse login(User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        User userFromDb = userRepository
                .findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Token refreshToken = tokenService.generateRefreshToken(userFromDb);

        String token = tokenService.generateToken(userFromDb, refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken.getToken())
                .tokenExpiration(tokenService.extractExpiration(token))
                .build();
    }

    public void logout(User user) {
        tokenService.revokeRefreshTokensByUser(user);
        SecurityContextHolder.clearContext();
    }
}
