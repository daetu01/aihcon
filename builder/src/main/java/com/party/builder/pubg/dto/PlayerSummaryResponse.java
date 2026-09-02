package com.party.builder.pubg.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PlayerSummaryResponse {
    private String accountId;
    private String nickname;
    private String shardId;
    private List<String> matchIds;
}
