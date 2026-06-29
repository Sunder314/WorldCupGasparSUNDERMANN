package com.fifa.worldcuppredictor.controller;

import com.fifa.worldcuppredictor.dto.ligue.LigueCreationRequest;
import com.fifa.worldcuppredictor.dto.ligue.LigueRejoindreRequest;
import com.fifa.worldcuppredictor.dto.ligue.LigueResponse;
import com.fifa.worldcuppredictor.security.UserPrincipal;
import com.fifa.worldcuppredictor.service.LigueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ligues")
@RequiredArgsConstructor
public class LigueController {

    private final LigueService ligueService;

    @PostMapping
    public ResponseEntity<LigueResponse> creer(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody LigueCreationRequest req
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ligueService.creer(user.getId(), req));
    }

    @PostMapping("/rejoindre")
    public ResponseEntity<LigueResponse> rejoindre(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody LigueRejoindreRequest req
    ) {
        return ResponseEntity.ok(ligueService.rejoindre(user.getId(), req));
    }

    @GetMapping("/mes-ligues")
    public ResponseEntity<List<LigueResponse>> mesLigues(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(ligueService.mesLigues(user.getId()));
    }
}
