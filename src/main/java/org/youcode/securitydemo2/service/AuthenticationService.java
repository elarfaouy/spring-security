package org.youcode.securitydemo2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.youcode.securitydemo2.domain.entity.User;
import org.youcode.securitydemo2.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public String register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);

        return tokenService.generateToken(saved);
    }

    public String login(User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        User userFromDb = userRepository
                .findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return tokenService.generateToken(userFromDb);
    }
}
