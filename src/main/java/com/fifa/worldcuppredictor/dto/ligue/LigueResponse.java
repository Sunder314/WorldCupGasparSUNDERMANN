package com.fifa.worldcuppredictor.dto.ligue;

import com.fifa.worldcuppredictor.entity.Ligue;

import java.time.LocalDateTime;

public record LigueResponse(
        Long id,
        String nom,
        String code,
        String createurPseudo,
        int nombreMembres,
        LocalDateTime creeLe
) {
    public static LigueResponse from(Ligue l) {
        return new LigueResponse(
                l.getId(),
                l.getNom(),
                l.getCode(),
                l.getCreateur().getPseudo(),
                l.getMembres().size(),
                l.getCreeLe()
        );
    }
}
