package com.party.builder.squad.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SquadMatchResponse {

    private int compatibilityScore;
    private TeamProfile teamProfile;
    private List<PlayerInfo> players;
    private List<String> strengths;
    private List<String> weaknesses;

    @Getter
    @Builder
    public static class TeamProfile {
        private int aggression;
        private int survival;
        private int support;
        private int mobility;
        private int combat;
    }

    @Getter
    @Builder
    public static class PlayerInfo {
        private String nickname;
        private String playStyle;

        private int aggression;
        private int survival;
        private int support;
        private int mobility;
        private int combat;
    }

}
