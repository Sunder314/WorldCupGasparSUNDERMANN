package com.fifa.worldcuppredictor.controller;

import com.fifa.worldcuppredictor.dto.pronostic.PronosticRequest;
import com.fifa.worldcuppredictor.dto.pronostic.PronosticResponse;
import com.fifa.worldcuppredictor.security.UserPrincipal;
import com.fifa.worldcuppredictor.service.PronosticService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pronostics")
@RequiredArgsConstructor
public class PronosticController {

    private final PronosticService pronosticService;

    @PostMapping
    public ResponseEntity<PronosticResponse> enregistrer(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody PronosticRequest req
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pronosticService.enregistrer(user.getId(), req));
    }

    @GetMapping("/mes-pronostics")
    public ResponseEntity<List<PronosticResponse>> mesPronostics(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(pronosticService.mesPronostics(user.getId()));
    }
}
