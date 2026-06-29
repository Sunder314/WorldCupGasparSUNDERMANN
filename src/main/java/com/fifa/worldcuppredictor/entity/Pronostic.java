package com.fifa.worldcuppredictor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pronostics", uniqueConstraints = {
        @UniqueConstraint(name = "uk_pronostic_user_match", columnNames = {"utilisateur_id", "match_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pronostic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private MatchRencontre match;

    @Column(name = "score_pred_a", nullable = false)
    private Integer scorePredA;

    @Column(name = "score_pred_b", nullable = false)
    private Integer scorePredB;

    @Column(name = "points_gagnes")
    private Integer pointsGagnes;

    @Column(name = "cree_le", nullable = false, updatable = false)
    private LocalDateTime creeLe;

    @PrePersist
    void onCreate() {
        if (creeLe == null) creeLe = LocalDateTime.now();
    }
}
