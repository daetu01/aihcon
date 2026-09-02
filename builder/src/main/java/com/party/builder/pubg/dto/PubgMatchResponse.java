package com.party.builder.pubg.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PubgMatchResponse {

    private MatchData data;
    private List<Included> included;

    @Getter
    public static class MatchData {
        private String id;
        private MatchAttributes attributes;
    }

    @Getter
    public static class MatchAttributes {
        private String gameMode;
        private String mapName;
        private String matchType;
        private int duration;
        private String createdAt;
    }

    @Getter
    public static class Included {
        private String type;
        private String id;
        private ParticipantAttributes attributes;
    }

    @Getter
    public static class ParticipantAttributes {
        private Stats stats;
        private String shardId;
    }

    @Getter
    public static class Stats {

        private int DBNOs;
        private int assists;
        private int boosts;

        private double damageDealt;

        private String deathType;

        private int headshotKills;
        private int heals;
        private int killPlace;
        private int kills;

        private double longestKill;

        private String name;
        private String playerId;

        private int revives;

        private double rideDistance;
        private double swimDistance;
        private double walkDistance;

        private int timeSurvived;
        private int weaponsAcquired;
        private int winPlace;
    }
}
