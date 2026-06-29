package com.fifa.worldcuppredictor.repository;

import com.fifa.worldcuppredictor.entity.Ligue;
import com.fifa.worldcuppredictor.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LigueRepository extends JpaRepository<Ligue, Long> {

    Optional<Ligue> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT l FROM Ligue l JOIN l.membres m WHERE m.id = :utilisateurId")
    List<Ligue> findAllByMembreId(@Param("utilisateurId") Long utilisateurId);

    @Query("SELECT m FROM Ligue l JOIN l.membres m WHERE l.id = :ligueId ORDER BY m.points DESC")
    List<Utilisateur> findMembresByLigueIdOrderByPointsDesc(@Param("ligueId") Long ligueId);
}
