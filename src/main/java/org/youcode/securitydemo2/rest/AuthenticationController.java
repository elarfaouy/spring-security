package org.youcode.securitydemo2.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.youcode.securitydemo2.domain.entity.User;
import org.youcode.securitydemo2.dto.AuthenticationResponse;
import org.youcode.securitydemo2.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

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

//    @PostMapping("/refresh-token")
//    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        return ResponseEntity.ok(
//                AuthenticationResponse.builder()
//                        .accessToken(tokenService.refreshToken(request, response))
//                        .refreshToken("refresh")
//                        .tokenExpiration("expiration")
//                        .build()
//        );
//    }
}
