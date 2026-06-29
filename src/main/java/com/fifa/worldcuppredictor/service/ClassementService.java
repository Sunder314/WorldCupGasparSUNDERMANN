package com.fifa.worldcuppredictor.service;

import com.fifa.worldcuppredictor.dto.classement.ClassementEntry;
import com.fifa.worldcuppredictor.entity.Utilisateur;
import com.fifa.worldcuppredictor.exception.ApiException;
import com.fifa.worldcuppredictor.repository.LigueRepository;
import com.fifa.worldcuppredictor.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassementService {

    private final UtilisateurRepository utilisateurRepository;
    private final LigueRepository ligueRepository;

    @Transactional(readOnly = true)
    public List<ClassementEntry> general() {
        return rang(utilisateurRepository.findAllByOrderByPointsDesc());
    }

    @Transactional(readOnly = true)
    public List<ClassementEntry> ligue(Long ligueId) {
        if (!ligueRepository.existsById(ligueId)) {
            throw ApiException.notFound("Ligue introuvable");
        }
        return rang(ligueRepository.findMembresByLigueIdOrderByPointsDesc(ligueId));
    }

    private List<ClassementEntry> rang(List<Utilisateur> us) {
        List<ClassementEntry> res = new ArrayList<>(us.size());
        for (int i = 0; i < us.size(); i++) {
            res.add(ClassementEntry.of(i + 1, us.get(i)));
        }
        return res;
    }
}
