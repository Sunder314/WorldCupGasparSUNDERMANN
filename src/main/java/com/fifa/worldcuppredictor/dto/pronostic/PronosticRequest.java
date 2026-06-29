package com.fifa.worldcuppredictor.dto.pronostic;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PronosticRequest(
        @NotNull Long matchId,
        @NotNull @Min(0) Integer scorePredA,
        @NotNull @Min(0) Integer scorePredB
) {}
