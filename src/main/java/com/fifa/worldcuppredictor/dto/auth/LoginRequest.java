package com.fifa.worldcuppredictor.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String pseudo,
        @NotBlank String motDePasse
) {}
