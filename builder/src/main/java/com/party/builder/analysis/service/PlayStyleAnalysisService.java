package com.party.builder.analysis.service;

import com.party.builder.analysis.dto.PlayerStyleProfile;
import com.party.builder.analysis.dto.TelemetryAggregateStats;
import com.party.builder.pubg.dto.PlayerAggregateStats;
import org.springframework.stereotype.Service;

@Service
public class PlayStyleAnalysisService {

    public PlayerStyleProfile analyze(
            PlayerAggregateStats stats,
            TelemetryAggregateStats telemetry
    ) {

        int killScore = normalize(stats.getAvgKills(), 5.0);
        int damageScore = normalize(stats.getAvgDamage(), 500.0);
        int dbnoScore = normalize(stats.getAvgDBNOs(), 4.0);

        int survivalTimeScore =
                normalize(stats.getAvgSurvivalTime(), 1800.0);

        int survivalRateScore =
                normalize(stats.getSurvivalRate(), 1.0);

        int top10Score =
                normalize(stats.getTop10Rate(), 1.0);

        int assistScore =
                normalize(stats.getAvgAssists(), 2.0);

        int reviveScore =
                normalize(stats.getAvgRevives(), 2.0);

        int walkScore =
                normalize(stats.getAvgWalkDistance(), 4000.0);

        int rideScore =
                normalize(stats.getAvgRideDistance(), 5000.0);

        int headshotScore =
                normalize(stats.getHeadshotRate(), 0.5);


        int engagementScore =
                normalize(telemetry.getAvgEngagementCount(), 5.0);

        int groggyScore =
                normalize(telemetry.getAvgGroggyCount(), 3.0);

        int aggression = weightedScore(
                damageScore, 0.30,
                killScore, 0.25,
                engagementScore, 0.25,
                groggyScore, 0.20
        );

        int survival = weightedScore(
                survivalTimeScore, 0.8,
                survivalRateScore, 0.2
        );

        int support = weightedScore(
                assistScore, 0.5,
                reviveScore, 0.5
        );

        int mobility = weightedScore(
                walkScore, 0.5,
                rideScore, 0.5
        );

        int combat = weightedScore(
                damageScore, 0.35,
                killScore, 0.30,
                groggyScore, 0.20,
                headshotScore, 0.15
        );

        String playStyle = classify(
                stats,
                aggression,
                survival,
                support,
                mobility,
                combat
        );

        return PlayerStyleProfile.builder()
                .aggression(aggression)
                .survival(survival)
                .support(support)
                .mobility(mobility)
                .combat(combat)
                .playStyle(playStyle)
                .build();
    }

    private int normalize(double value, double maxValue) {

        if (value <= 0) {
            return 0;
        }

        return (int) Math.min(
                100,
                (value / maxValue) * 100
        );
    }

    private int weightedScore(
            int score1, double weight1,
            int score2, double weight2
    ) {
        return (int) (
                score1 * weight1 +
                        score2 * weight2
        );
    }

    private int weightedScore(
            int score1, double weight1,
            int score2, double weight2,
            int score3, double weight3,
            int score4, double weight4
    ) {
        return (int) (
                score1 * weight1 +
                        score2 * weight2 +
                        score3 * weight3 +
                        score4 * weight4
        );
    }

    private String classify(
            PlayerAggregateStats stats,
            int aggression,
            int survival,
            int support,
            int mobility,
            int combat
    ) {

        if (stats.getMatchesAnalyzed() < 10) {
            return "INSUFFICIENT_DATA";
        }

        if (aggression >= 70 && combat >= 70) {
            return "ENTRY_FRAGGER";
        }

        if (support >= 65) {
            return "SUPPORT";
        }

        if (survival >= 75 && aggression < 50) {
            return "SURVIVOR";
        }

        if (mobility >= 70) {
            return "SCOUT";
        }

        return "FLEX";
    }
}
