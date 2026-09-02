package com.party.builder.pubg.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlayerAggregateStats {

    private int matchesAnalyzed;

    private double avgKills;
    private double avgDamage;
    private double avgDBNOs;
    private double avgAssists;
    private double avgRevives;

    private double avgSurvivalTime;
    private double avgWalkDistance;
    private double avgRideDistance;
    private double survivalRate;

    private double top10Rate;
    private double headshotRate;
}
