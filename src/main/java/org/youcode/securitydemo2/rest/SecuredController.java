package org.youcode.securitydemo2.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/secured")
public class SecuredController {
    @GetMapping
    public ResponseEntity<Map<String, Object>> secured() {
        return ResponseEntity.ok(Map.of("message", "This is a secured endpoint"));
    }
}
