package com.fifa.worldcuppredictor.repository;

import com.fifa.worldcuppredictor.entity.MatchRencontre;
import com.fifa.worldcuppredictor.entity.StatutMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchRencontre, Long> {

    List<MatchRencontre> findAllByStatut(StatutMatch statut);

    List<MatchRencontre> findAllByOrderByDateCoupEnvoiAsc();
}
