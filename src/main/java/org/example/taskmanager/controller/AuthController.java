package org.example.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.example.taskmanager.dto.NewUserRequest;
import org.example.taskmanager.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody NewUserRequest request) {
        return ResponseEntity.ok( authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody NewUserRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }
}