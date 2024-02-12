package org.youcode.securitydemo2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

        String token = tokenService.generateToken(saved);

        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken("refresh")
                .tokenExpiration(tokenService.extractExpiration(token))
                .build();
    }

    public AuthenticationResponse login(User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        User userFromDb = userRepository
                .findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = tokenService.generateToken(userFromDb);
        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken("refresh")
                .tokenExpiration(tokenService.extractExpiration(token))
                .build();
    }
}
