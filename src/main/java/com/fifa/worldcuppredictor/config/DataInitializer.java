package com.fifa.worldcuppredictor.config;

import com.fifa.worldcuppredictor.entity.MatchRencontre;
import com.fifa.worldcuppredictor.entity.Role;
import com.fifa.worldcuppredictor.entity.StatutMatch;
import com.fifa.worldcuppredictor.entity.Utilisateur;
import com.fifa.worldcuppredictor.repository.MatchRepository;
import com.fifa.worldcuppredictor.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final MatchRepository matchRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!utilisateurRepository.existsByPseudo("admin")) {
            Utilisateur admin = Utilisateur.builder()
                    .pseudo("admin")
                    .email("admin@fifa.local")
                    .motDePasse(passwordEncoder.encode("admin123"))
                    .points(0)
                    .role(Role.ADMIN)
                    .build();
            utilisateurRepository.save(admin);
            log.info("Admin par défaut créé — pseudo: admin / mot de passe: admin123");
        }

        if (matchRepository.count() == 0) {
            matchRepository.save(MatchRencontre.builder()
                    .equipeA("France").equipeB("Brésil")
                    .dateCoupEnvoi(LocalDateTime.now().plusDays(1))
                    .statut(StatutMatch.A_VENIR).build());
            matchRepository.save(MatchRencontre.builder()
                    .equipeA("Argentine").equipeB("Allemagne")
                    .dateCoupEnvoi(LocalDateTime.now().plusDays(2))
                    .statut(StatutMatch.A_VENIR).build());
            matchRepository.save(MatchRencontre.builder()
                    .equipeA("Espagne").equipeB("Portugal")
                    .dateCoupEnvoi(LocalDateTime.now().plusDays(3))
                    .statut(StatutMatch.A_VENIR).build());
            log.info("3 matchs de démo créés");
        }
    }
}
