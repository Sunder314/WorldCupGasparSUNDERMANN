package com.fifa.worldcuppredictor.dto.ligue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LigueCreationRequest(
        @NotBlank @Size(min = 3, max = 80) String nom
) {}
