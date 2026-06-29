package com.fifa.worldcuppredictor.controller;

import com.fifa.worldcuppredictor.dto.auth.AuthResponse;
import com.fifa.worldcuppredictor.dto.auth.LoginRequest;
import com.fifa.worldcuppredictor.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}
