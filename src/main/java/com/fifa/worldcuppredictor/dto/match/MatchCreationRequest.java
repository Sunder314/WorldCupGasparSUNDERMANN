package com.fifa.worldcuppredictor.dto.match;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MatchCreationRequest(
        @NotBlank String equipeA,
        @NotBlank String equipeB,
        @NotNull @Future LocalDateTime dateCoupEnvoi
) {}
