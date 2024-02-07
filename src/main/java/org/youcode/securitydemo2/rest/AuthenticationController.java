package org.youcode.securitydemo2.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.youcode.securitydemo2.domain.entity.User;
import org.youcode.securitydemo2.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public String register(@RequestBody User user){
        return authenticationService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        return authenticationService.login(user);
    }
}
