package org.youcode.securitydemo2.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.youcode.securitydemo2.domain.entity.Permission;
import org.youcode.securitydemo2.domain.entity.User;
import org.youcode.securitydemo2.dto.AuthenticationResponse;
import org.youcode.securitydemo2.dto.RefreshTokenRequest;
import org.youcode.securitydemo2.dto.UserInfoResponse;
import org.youcode.securitydemo2.service.AuthenticationService;
import org.youcode.securitydemo2.service.TokenService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User user) {
        return ResponseEntity.ok(
                authenticationService.register(user)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User user) {
        return ResponseEntity.ok(
                authenticationService.login(user)
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(
                tokenService.generateNewAccessToken(refreshTokenRequest.getRefreshToken())
        );
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUser() throws RuntimeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User user) {
            return ResponseEntity.ok(
                    UserInfoResponse.builder()
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .role(user.getRole().getName())
                            .permissions(user.getRole().getPermissions().stream().map(Permission::getName).toList())
                            .build()
            );
        }

        return ResponseEntity.ok(
                UserInfoResponse.builder()
                        .username("anonymous")
                        .role("anonymous")
                        .build()
        );
    }
}
