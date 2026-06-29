package com.fifa.worldcuppredictor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "utilisateurs", uniqueConstraints = {
        @UniqueConstraint(name = "uk_utilisateur_pseudo", columnNames = "pseudo"),
        @UniqueConstraint(name = "uk_utilisateur_email", columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String pseudo;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Column(nullable = false)
    @Builder.Default
    private Integer points = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.USER;

    @ManyToMany(mappedBy = "membres", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Ligue> ligues = new HashSet<>();
}
