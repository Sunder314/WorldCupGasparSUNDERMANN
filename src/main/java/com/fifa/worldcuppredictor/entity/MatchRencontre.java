package com.fifa.worldcuppredictor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matchs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchRencontre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipe_a", nullable = false, length = 80)
    private String equipeA;

    @Column(name = "equipe_b", nullable = false, length = 80)
    private String equipeB;

    @Column(name = "date_coup_envoi", nullable = false)
    private LocalDateTime dateCoupEnvoi;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatutMatch statut = StatutMatch.A_VENIR;

    @Column(name = "score_a")
    private Integer scoreA;

    @Column(name = "score_b")
    private Integer scoreB;
}
