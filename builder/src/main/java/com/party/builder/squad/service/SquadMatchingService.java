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
                        .aggression(
                                profiles.stream()
                                        .mapToInt(PlayerStyleProfile::getAggression)
                                        .max()
                                        .orElse(0)
                        )
                        .survival(
                                profiles.stream()
                                        .mapToInt(PlayerStyleProfile::getSurvival)
                                        .max()
                                        .orElse(0)
                        )
                        .support(
                                profiles.stream()
                                        .mapToInt(PlayerStyleProfile::getSupport)
                                        .max()
                                        .orElse(0)
                        )
                        .mobility(
                                profiles.stream()
                                        .mapToInt(PlayerStyleProfile::getMobility)
                                        .max()
                                        .orElse(0)
                        )
                        .combat(
                                profiles.stream()
                                        .mapToInt(PlayerStyleProfile::getCombat)
                                        .max()
                                        .orElse(0)
                        )
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

        Set<String> styles = new HashSet<>();

        for (PlayerStyleProfile profile : profiles) {
            styles.add(profile.getPlayStyle());
        }

        if (styles.size() >= 3) {
            strengths.add("서로 다른 플레이 스타일이 잘 조합되어 있습니다.");
        }

        if (styles.contains("ENTRY_FRAGGER")
                && styles.contains("SUPPORT")) {
            strengths.add("공격과 지원 역할의 균형이 좋습니다.");
        }

        if (styles.contains("SURVIVOR")) {
            strengths.add("팀의 생존 안정성이 높습니다.");
        }

        if (styles.contains("SCOUT")) {
            strengths.add("정찰 및 이동 역할을 수행할 수 있습니다.");
        }

        if (styles.contains("SHARPSHOOTER")) {
            strengths.add("안정적인 교전 화력을 기대할 수 있습니다.");
        }

        return strengths;
    }

    private List<String> analyzeWeaknesses(
            List<PlayerStyleProfile> profiles
    ) {

        List<String> weaknesses = new ArrayList<>();

        Set<String> styles = new HashSet<>();

        for (PlayerStyleProfile profile : profiles) {
            styles.add(profile.getPlayStyle());
        }

        if (!styles.contains("ENTRY_FRAGGER")) {
            weaknesses.add("적극적으로 교전을 여는 역할이 부족합니다.");
        }

        if (!styles.contains("SUPPORT")) {
            weaknesses.add("지원 역할이 부족합니다.");
        }

        if (!styles.contains("SURVIVOR")) {
            weaknesses.add("생존 안정성이 부족할 수 있습니다.");
        }

        if (!styles.contains("SCOUT")) {
            weaknesses.add("정찰 및 이동 역할이 부족할 수 있습니다.");
        }

        return weaknesses;
    }
}
