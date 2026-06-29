package com.fifa.worldcuppredictor.service;

import com.fifa.worldcuppredictor.dto.ligue.LigueCreationRequest;
import com.fifa.worldcuppredictor.dto.ligue.LigueRejoindreRequest;
import com.fifa.worldcuppredictor.dto.ligue.LigueResponse;
import com.fifa.worldcuppredictor.entity.Ligue;
import com.fifa.worldcuppredictor.entity.Utilisateur;
import com.fifa.worldcuppredictor.exception.ApiException;
import com.fifa.worldcuppredictor.repository.LigueRepository;
import com.fifa.worldcuppredictor.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LigueService {

    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 8;
    private static final int MAX_ATTEMPTS = 10;
    private final SecureRandom random = new SecureRandom();

    private final LigueRepository ligueRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Transactional
    public LigueResponse creer(Long utilisateurId, LigueCreationRequest req) {
        Utilisateur createur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> ApiException.unauthorized("Utilisateur introuvable"));

        Ligue ligue = Ligue.builder()
                .nom(req.nom())
                .code(genererCodeUnique())
                .createur(createur)
                .build();
        ligue.getMembres().add(createur);

        return LigueResponse.from(ligueRepository.save(ligue));
    }

    @Transactional
    public LigueResponse rejoindre(Long utilisateurId, LigueRejoindreRequest req) {
        Ligue ligue = ligueRepository.findByCode(req.code().toUpperCase())
                .orElseThrow(() -> ApiException.notFound("Aucune ligue avec ce code"));

        Utilisateur u = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> ApiException.unauthorized("Utilisateur introuvable"));

        if (!ligue.getMembres().add(u)) {
            throw ApiException.conflict("Vous êtes déjà membre de cette ligue");
        }

        return LigueResponse.from(ligueRepository.save(ligue));
    }

    @Transactional(readOnly = true)
    public List<LigueResponse> mesLigues(Long utilisateurId) {
        return ligueRepository.findAllByMembreId(utilisateurId).stream()
                .map(LigueResponse::from)
                .toList();
    }

    private String genererCodeUnique() {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            StringBuilder sb = new StringBuilder(CODE_LENGTH);
            for (int j = 0; j < CODE_LENGTH; j++) {
                sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
            }
            String code = sb.toString();
            if (!ligueRepository.existsByCode(code)) {
                return code;
            }
        }
        throw ApiException.conflict("Impossible de générer un code de ligue unique");
    }
}
