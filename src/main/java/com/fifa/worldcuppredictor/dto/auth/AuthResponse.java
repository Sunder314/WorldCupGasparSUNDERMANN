package com.fifa.worldcuppredictor.dto.auth;

public record AuthResponse(
        String token,
        String tokenType,
        Long utilisateurId,
        String pseudo,
        String role
) {
    public static AuthResponse bearer(String token, Long id, String pseudo, String role) {
        return new AuthResponse(token, "Bearer", id, pseudo, role);
    }
}
