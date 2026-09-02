package com.party.builder.analysis.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlayerStyleProfile {

    private int aggression;
    private int survival;
    private int support;
    private int mobility;
    private int combat;

    private String playStyle;
}
