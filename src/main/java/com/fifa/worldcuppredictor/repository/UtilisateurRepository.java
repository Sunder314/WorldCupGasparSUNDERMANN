package com.fifa.worldcuppredictor.repository;

import com.fifa.worldcuppredictor.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByPseudo(String pseudo);

    Optional<Utilisateur> findByEmail(String email);

    boolean existsByPseudo(String pseudo);

    boolean existsByEmail(String email);

    List<Utilisateur> findAllByOrderByPointsDesc();
}
