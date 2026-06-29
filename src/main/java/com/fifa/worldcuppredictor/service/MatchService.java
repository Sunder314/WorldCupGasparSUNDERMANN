package com.fifa.worldcuppredictor.service;

import com.fifa.worldcuppredictor.dto.match.MatchCreationRequest;
import com.fifa.worldcuppredictor.dto.match.MatchResponse;
import com.fifa.worldcuppredictor.dto.match.ScoreRequest;
import com.fifa.worldcuppredictor.entity.MatchRencontre;
import com.fifa.worldcuppredictor.entity.Pronostic;
import com.fifa.worldcuppredictor.entity.StatutMatch;
import com.fifa.worldcuppredictor.exception.ApiException;
import com.fifa.worldcuppredictor.repository.MatchRepository;
import com.fifa.worldcuppredictor.repository.PronosticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final PronosticRepository pronosticRepository;
    private final PointsService pointsService;

    @Transactional(readOnly = true)
    public List<MatchResponse> lister(StatutMatch statut) {
        List<MatchRencontre> matchs = (statut == null)
                ? matchRepository.findAllByOrderByDateCoupEnvoiAsc()
                : matchRepository.findAllByStatut(statut);
        return matchs.stream().map(MatchResponse::from).toList();
    }

    @Transactional
    public MatchResponse creer(MatchCreationRequest req) {
        MatchRencontre m = MatchRencontre.builder()
                .equipeA(req.equipeA())
                .equipeB(req.equipeB())
                .dateCoupEnvoi(req.dateCoupEnvoi())
                .statut(StatutMatch.A_VENIR)
                .build();
        return MatchResponse.from(matchRepository.save(m));
    }

    @Transactional
    public MatchResponse enregistrerScore(Long matchId, ScoreRequest req) {
        MatchRencontre m = matchRepository.findById(matchId)
                .orElseThrow(() -> ApiException.notFound("Match introuvable"));

        boolean revalidation = m.getStatut() == StatutMatch.TERMINE;

        m.setScoreA(req.scoreA());
        m.setScoreB(req.scoreB());
        m.setStatut(StatutMatch.TERMINE);

        List<Pronostic> pronos = pronosticRepository.findAllByMatchId(matchId);
        for (Pronostic p : pronos) {
            if (revalidation && p.getPointsGagnes() != null) {
                p.getUtilisateur().setPoints(p.getUtilisateur().getPoints() - p.getPointsGagnes());
            }
            int gagnes = pointsService.calculer(p, req.scoreA(), req.scoreB());
            p.setPointsGagnes(gagnes);
            p.getUtilisateur().setPoints(p.getUtilisateur().getPoints() + gagnes);
        }

        return MatchResponse.from(matchRepository.save(m));
    }
}
