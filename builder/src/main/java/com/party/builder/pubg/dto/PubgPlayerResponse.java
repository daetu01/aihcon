package com.party.builder.pubg.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PubgPlayerResponse {

    private List<PlayerData> data;

    @Getter
    public static class PlayerData {
        private String type;
        private String id;
        private Attributes attributes;
        private Relationships relationships;
    }

    @Getter
    public static class Attributes {
        private String name;
        private String shardId;
        private String clanId;
        private String banType;
    }

    @Getter
    public static class Relationships {
        private Matches matches;
    }

    @Getter
    public static class Matches {
        private List<MatchData> data;
    }

    @Getter
    public static class MatchData {
        private String type;
        private String id;
    }
}
