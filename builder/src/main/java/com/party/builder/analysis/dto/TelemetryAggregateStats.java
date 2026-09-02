package com.party.builder.analysis.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TelemetryAggregateStats {
    private int matchesAnalyzed;

    private double avgAttackCount;
    private double avgEngagementCount;
    private double avgGroggyCount;
    private double avgKillCount;
    private double avgReviveCount;
}
