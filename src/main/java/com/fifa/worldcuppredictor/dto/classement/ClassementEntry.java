package com.fifa.worldcuppredictor.dto.classement;

import com.fifa.worldcuppredictor.entity.Utilisateur;

public record ClassementEntry(
        int rang,
        Long utilisateurId,
        String pseudo,
        Integer points
) {
    public static ClassementEntry of(int rang, Utilisateur u) {
        return new ClassementEntry(rang, u.getId(), u.getPseudo(), u.getPoints());
    }
}
