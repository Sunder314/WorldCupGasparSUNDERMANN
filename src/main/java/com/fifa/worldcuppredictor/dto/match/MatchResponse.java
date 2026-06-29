package com.fifa.worldcuppredictor.dto.match;

import com.fifa.worldcuppredictor.entity.MatchRencontre;
import com.fifa.worldcuppredictor.entity.StatutMatch;

import java.time.LocalDateTime;

public record MatchResponse(
        Long id,
        String equipeA,
        String equipeB,
        LocalDateTime dateCoupEnvoi,
        StatutMatch statut,
        Integer scoreA,
        Integer scoreB
) {
    public static MatchResponse from(MatchRencontre m) {
        return new MatchResponse(
                m.getId(),
                m.getEquipeA(),
                m.getEquipeB(),
                m.getDateCoupEnvoi(),
                m.getStatut(),
                m.getScoreA(),
                m.getScoreB()
        );
    }
}
