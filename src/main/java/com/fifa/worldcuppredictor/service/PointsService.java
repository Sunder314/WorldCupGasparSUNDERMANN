package com.fifa.worldcuppredictor.service;

import com.fifa.worldcuppredictor.entity.Pronostic;
import org.springframework.stereotype.Service;

@Service
public class PointsService {

    public static final int POINTS_BONNE_ISSUE = 100;
    public static final int BONUS_SCORE_EXACT = 50;

    public int calculer(Pronostic prono, int scoreA, int scoreB) {
        int signeReel = Integer.signum(scoreA - scoreB);
        int signePred = Integer.signum(prono.getScorePredA() - prono.getScorePredB());

        if (signeReel != signePred) {
            return 0;
        }

        boolean exact = prono.getScorePredA() == scoreA && prono.getScorePredB() == scoreB;
        return exact ? POINTS_BONNE_ISSUE + BONUS_SCORE_EXACT : POINTS_BONNE_ISSUE;
    }
}
