package org.youcode.securitydemo2.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secured")
public class SecuredController {
    @GetMapping
    public ResponseEntity<String> secured() {
        return ResponseEntity.ok("Secured");
    }
}
