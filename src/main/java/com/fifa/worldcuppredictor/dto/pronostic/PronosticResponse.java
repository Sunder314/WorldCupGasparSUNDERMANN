package com.fifa.worldcuppredictor.dto.pronostic;

import com.fifa.worldcuppredictor.entity.Pronostic;
import com.fifa.worldcuppredictor.entity.StatutMatch;

import java.time.LocalDateTime;

public record PronosticResponse(
        Long id,
        Long matchId,
        String equipeA,
        String equipeB,
        LocalDateTime dateCoupEnvoi,
        StatutMatch statutMatch,
        Integer scorePredA,
        Integer scorePredB,
        Integer scoreReelA,
        Integer scoreReelB,
        Integer pointsGagnes,
        LocalDateTime creeLe
) {
    public static PronosticResponse from(Pronostic p) {
        return new PronosticResponse(
                p.getId(),
                p.getMatch().getId(),
                p.getMatch().getEquipeA(),
                p.getMatch().getEquipeB(),
                p.getMatch().getDateCoupEnvoi(),
                p.getMatch().getStatut(),
                p.getScorePredA(),
                p.getScorePredB(),
                p.getMatch().getScoreA(),
                p.getMatch().getScoreB(),
                p.getPointsGagnes(),
                p.getCreeLe()
        );
    }
}
