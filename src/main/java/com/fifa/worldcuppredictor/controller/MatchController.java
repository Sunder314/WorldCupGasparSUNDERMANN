package com.fifa.worldcuppredictor.controller;

import com.fifa.worldcuppredictor.dto.match.MatchCreationRequest;
import com.fifa.worldcuppredictor.dto.match.MatchResponse;
import com.fifa.worldcuppredictor.dto.match.ScoreRequest;
import com.fifa.worldcuppredictor.entity.StatutMatch;
import com.fifa.worldcuppredictor.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matchs")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public ResponseEntity<List<MatchResponse>> lister(@RequestParam(required = false) StatutMatch statut) {
        return ResponseEntity.ok(matchService.lister(statut));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MatchResponse> creer(@Valid @RequestBody MatchCreationRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matchService.creer(req));
    }

    @PutMapping("/{id}/score")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MatchResponse> enregistrerScore(
            @PathVariable Long id,
            @Valid @RequestBody ScoreRequest req
    ) {
        return ResponseEntity.ok(matchService.enregistrerScore(id, req));
    }
}
