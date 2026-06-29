package com.fifa.worldcuppredictor.repository;

import com.fifa.worldcuppredictor.entity.Pronostic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PronosticRepository extends JpaRepository<Pronostic, Long> {

    List<Pronostic> findAllByUtilisateurIdOrderByCreeLeDesc(Long utilisateurId);

    List<Pronostic> findAllByMatchId(Long matchId);

    Optional<Pronostic> findByUtilisateurIdAndMatchId(Long utilisateurId, Long matchId);

    boolean existsByUtilisateurIdAndMatchId(Long utilisateurId, Long matchId);
}
