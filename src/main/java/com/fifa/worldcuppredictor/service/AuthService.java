package com.fifa.worldcuppredictor.service;

import com.fifa.worldcuppredictor.dto.auth.AuthResponse;
import com.fifa.worldcuppredictor.dto.auth.InscriptionRequest;
import com.fifa.worldcuppredictor.dto.auth.LoginRequest;
import com.fifa.worldcuppredictor.entity.Role;
import com.fifa.worldcuppredictor.entity.Utilisateur;
import com.fifa.worldcuppredictor.exception.ApiException;
import com.fifa.worldcuppredictor.repository.UtilisateurRepository;
import com.fifa.worldcuppredictor.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse inscrire(InscriptionRequest req) {
        if (utilisateurRepository.existsByPseudo(req.pseudo())) {
            throw ApiException.conflict("Pseudo déjà utilisé");
        }
        if (utilisateurRepository.existsByEmail(req.email())) {
            throw ApiException.conflict("Email déjà utilisé");
        }

        Utilisateur u = Utilisateur.builder()
                .pseudo(req.pseudo())
                .email(req.email())
                .motDePasse(passwordEncoder.encode(req.motDePasse()))
                .points(0)
                .role(Role.USER)
                .build();
        utilisateurRepository.save(u);

        String token = jwtService.generateToken(u.getPseudo(), u.getId(), u.getRole().name());
        return AuthResponse.bearer(token, u.getId(), u.getPseudo(), u.getRole().name());
    }

    public AuthResponse login(LoginRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.pseudo(), req.motDePasse())
            );
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw ApiException.unauthorized("Identifiants invalides");
        }

        Utilisateur u = utilisateurRepository.findByPseudo(req.pseudo())
                .orElseThrow(() -> ApiException.unauthorized("Identifiants invalides"));

        String token = jwtService.generateToken(u.getPseudo(), u.getId(), u.getRole().name());
        return AuthResponse.bearer(token, u.getId(), u.getPseudo(), u.getRole().name());
    }
}
