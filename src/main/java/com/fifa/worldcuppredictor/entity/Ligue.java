package com.fifa.worldcuppredictor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ligues", uniqueConstraints = {
        @UniqueConstraint(name = "uk_ligue_code", columnNames = "code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ligue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nom;

    @Column(nullable = false, length = 12)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "createur_id", nullable = false)
    private Utilisateur createur;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ligue_membres",
            joinColumns = @JoinColumn(name = "ligue_id"),
            inverseJoinColumns = @JoinColumn(name = "utilisateur_id")
    )
    @Builder.Default
    private Set<Utilisateur> membres = new HashSet<>();

    @Column(name = "cree_le", nullable = false, updatable = false)
    private LocalDateTime creeLe;

    @PrePersist
    void onCreate() {
        if (creeLe == null) creeLe = LocalDateTime.now();
    }
}
