package com.fifa.worldcuppredictor.dto.ligue;

import jakarta.validation.constraints.NotBlank;

public record LigueRejoindreRequest(
        @NotBlank String code
) {}
