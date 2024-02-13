package org.youcode.securitydemo2.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/secured")
@PreAuthorize("hasRole('ADMIN')")
public class SecuredAdminController {
    @GetMapping
    @PreAuthorize("hasAuthority('Can Read Admin')")
    public ResponseEntity<Map<String, Object>> secured() {
        return ResponseEntity.ok(Map.of("message", "GET: This is a secured admin endpoint"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Can Write Admin')")
    public ResponseEntity<Map<String, Object>> securedPost() {
        return ResponseEntity.ok(Map.of("message", "POST: This is a secured admin endpoint"));
    }
}
