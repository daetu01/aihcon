package com.party.builder.analysis.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TelemetryFeatures {
    private int attackCount;
    private int engagementCount;

    private int groggyCount;
    private int killCount;
    private int reviveCount;
}
