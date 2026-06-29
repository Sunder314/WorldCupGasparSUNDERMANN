package com.fifa.worldcuppredictor.controller;

import com.fifa.worldcuppredictor.dto.auth.AuthResponse;
import com.fifa.worldcuppredictor.dto.auth.InscriptionRequest;
import com.fifa.worldcuppredictor.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final AuthService authService;

    @PostMapping("/inscription")
    public ResponseEntity<AuthResponse> inscription(@Valid @RequestBody InscriptionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.inscrire(req));
    }
}
