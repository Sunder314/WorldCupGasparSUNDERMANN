package com.fifa.worldcuppredictor.service;

import com.fifa.worldcuppredictor.dto.pronostic.PronosticRequest;
import com.fifa.worldcuppredictor.dto.pronostic.PronosticResponse;
import com.fifa.worldcuppredictor.entity.MatchRencontre;
import com.fifa.worldcuppredictor.entity.Pronostic;
import com.fifa.worldcuppredictor.entity.StatutMatch;
import com.fifa.worldcuppredictor.entity.Utilisateur;
import com.fifa.worldcuppredictor.exception.ApiException;
import com.fifa.worldcuppredictor.repository.MatchRepository;
import com.fifa.worldcuppredictor.repository.PronosticRepository;
import com.fifa.worldcuppredictor.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PronosticService {

    private final PronosticRepository pronosticRepository;
    private final MatchRepository matchRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Transactional
    public PronosticResponse enregistrer(Long utilisateurId, PronosticRequest req) {
        MatchRencontre m = matchRepository.findById(req.matchId())
                .orElseThrow(() -> ApiException.notFound("Match introuvable"));

        if (m.getStatut() != StatutMatch.A_VENIR || !m.getDateCoupEnvoi().isAfter(LocalDateTime.now())) {
            throw ApiException.badRequest("Le pronostic est verrouillé : le match a commencé ou est terminé");
        }

        Utilisateur u = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> ApiException.unauthorized("Utilisateur introuvable"));

        Pronostic p = pronosticRepository
                .findByUtilisateurIdAndMatchId(utilisateurId, req.matchId())
                .orElseGet(() -> Pronostic.builder().utilisateur(u).match(m).build());

        p.setScorePredA(req.scorePredA());
        p.setScorePredB(req.scorePredB());

        return PronosticResponse.from(pronosticRepository.save(p));
    }

    @Transactional(readOnly = true)
    public List<PronosticResponse> mesPronostics(Long utilisateurId) {
        return pronosticRepository.findAllByUtilisateurIdOrderByCreeLeDesc(utilisateurId).stream()
                .map(PronosticResponse::from)
                .toList();
    }
}
