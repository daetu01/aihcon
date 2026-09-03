package com.party.builder.squad.service;

import com.party.builder.analysis.dto.PlayerStyleProfile;
import com.party.builder.squad.dto.SquadMatchResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SquadMatchingService {

    public SquadMatchResponse analyze(
            List<String> nicknames,
            List<PlayerStyleProfile> profiles
    ) {

        int compatibilityScore =
                calculateCompatibility(profiles);

        List<SquadMatchResponse.PlayerInfo> players =
                new ArrayList<>();

        for (int i = 0; i < profiles.size(); i++) {

            players.add(
                    SquadMatchResponse.PlayerInfo.builder()
                            .nickname(nicknames.get(i))
                            .playStyle(profiles.get(i).getPlayStyle())
                            .aggression(profiles.get(i).getAggression())
                            .survival(profiles.get(i).getSurvival())
                            .support(profiles.get(i).getSupport())
                            .mobility(profiles.get(i).getMobility())
                            .combat(profiles.get(i).getCombat())
                            .build()
            );
        }

        List<String> strengths =
                analyzeStrengths(profiles);

        List<String> weaknesses =
                analyzeWeaknesses(profiles);

        SquadMatchResponse.TeamProfile teamProfile =
                SquadMatchResponse.TeamProfile.builder()
                        .aggression((int) profiles.stream()
                                .mapToInt(PlayerStyleProfile::getAggression)
                                .average()
                                .orElse(0))
                        .survival((int) profiles.stream()
                                .mapToInt(PlayerStyleProfile::getSurvival)
                                .average()
                                .orElse(0))
                        .support((int) profiles.stream()
                                .mapToInt(PlayerStyleProfile::getSupport)
                                .average()
                                .orElse(0))
                        .mobility((int) profiles.stream()
                                .mapToInt(PlayerStyleProfile::getMobility)
                                .average()
                                .orElse(0))
                        .combat((int) profiles.stream()
                                .mapToInt(PlayerStyleProfile::getCombat)
                                .average()
                                .orElse(0))
                        .build();

        return SquadMatchResponse.builder()
                .compatibilityScore(compatibilityScore)
                .teamProfile(teamProfile)
                .players(players)
                .strengths(strengths)
                .weaknesses(weaknesses)
                .build();
    }

    private int calculateCompatibility(
            List<PlayerStyleProfile> profiles
    ) {

        int maxAggression = profiles.stream()
                .mapToInt(PlayerStyleProfile::getAggression)
                .max()
                .orElse(0);

        int maxSurvival = profiles.stream()
                .mapToInt(PlayerStyleProfile::getSurvival)
                .max()
                .orElse(0);

        int maxSupport = profiles.stream()
                .mapToInt(PlayerStyleProfile::getSupport)
                .max()
                .orElse(0);

        int maxMobility = profiles.stream()
                .mapToInt(PlayerStyleProfile::getMobility)
                .max()
                .orElse(0);

        int maxCombat = profiles.stream()
                .mapToInt(PlayerStyleProfile::getCombat)
                .max()
                .orElse(0);

        int coverageScore =
                (maxAggression
                        + maxSurvival
                        + maxSupport
                        + maxMobility
                        + maxCombat) / 5;

        Set<String> styles = profiles.stream()
                .map(PlayerStyleProfile::getPlayStyle)
                .filter(style -> !"INSUFFICIENT_DATA".equals(style))
                .collect(Collectors.toSet());

        int roleDiversityScore = switch (styles.size()) {
            case 4 -> 100;
            case 3 -> 80;
            case 2 -> 60;
            case 1 -> 40;
            default -> 0;
        };

        int finalScore = (int) (
                coverageScore * 0.7
                        + roleDiversityScore * 0.3
        );

        return Math.min(finalScore, 100);
    }

    private List<String> analyzeStrengths(
            List<PlayerStyleProfile> profiles
    ) {

        List<String> strengths = new ArrayList<>();

        double avgAggression = profiles.stream()
                .mapToInt(PlayerStyleProfile::getAggression)
                .average()
                .orElse(0);

        double avgSurvival = profiles.stream()
                .mapToInt(PlayerStyleProfile::getSurvival)
                .average()
                .orElse(0);

        double avgSupport = profiles.stream()
                .mapToInt(PlayerStyleProfile::getSupport)
                .average()
                .orElse(0);

        double avgMobility = profiles.stream()
                .mapToInt(PlayerStyleProfile::getMobility)
                .average()
                .orElse(0);

        double avgCombat = profiles.stream()
                .mapToInt(PlayerStyleProfile::getCombat)
                .average()
                .orElse(0);

        if (avgAggression >= 60 && avgCombat >= 60) {
            strengths.add("팀 전체의 교전 능력이 좋습니다.");
        }

        if (avgSupport >= 60) {
            strengths.add("팀원 간 지원 능력이 좋습니다.");
        }

        if (avgSurvival >= 60) {
            strengths.add("팀의 생존 안정성이 높습니다.");
        }

        if (avgMobility >= 60) {
            strengths.add("팀의 이동 및 포지셔닝 능력이 좋습니다.");
        }

        return strengths;
    }

    private List<String> analyzeWeaknesses(
            List<PlayerStyleProfile> profiles
    ) {

        List<String> weaknesses = new ArrayList<>();

        double avgAggression = profiles.stream()
                .mapToInt(PlayerStyleProfile::getAggression)
                .average()
                .orElse(0);

        double avgSurvival = profiles.stream()
                .mapToInt(PlayerStyleProfile::getSurvival)
                .average()
                .orElse(0);

        double avgSupport = profiles.stream()
                .mapToInt(PlayerStyleProfile::getSupport)
                .average()
                .orElse(0);

        double avgMobility = profiles.stream()
                .mapToInt(PlayerStyleProfile::getMobility)
                .average()
                .orElse(0);

        double avgCombat = profiles.stream()
                .mapToInt(PlayerStyleProfile::getCombat)
                .average()
                .orElse(0);

        if (avgAggression < 40) {
            weaknesses.add("적극적으로 교전을 여는 능력이 부족합니다.");
        }

        if (avgCombat < 40) {
            weaknesses.add("전반적인 교전 화력이 부족합니다.");
        }

        if (avgSupport < 40) {
            weaknesses.add("지원 역할이 부족합니다.");
        }

        if (avgSurvival < 40) {
            weaknesses.add("전체적인 생존 안정성이 낮습니다.");
        }

        if (avgMobility < 40) {
            weaknesses.add("이동 및 포지셔닝 능력이 부족할 수 있습니다.");
        }

        return weaknesses;
    }
}
