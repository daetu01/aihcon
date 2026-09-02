package com.party.builder.pubg.controller;


import com.party.builder.analysis.dto.PlayerStyleProfile;
import com.party.builder.analysis.dto.TelemetryAggregateStats;
import com.party.builder.analysis.dto.TelemetryFeatures;
import com.party.builder.pubg.dto.PlayerAggregateStats;
import com.party.builder.pubg.dto.PlayerSummaryResponse;
import com.party.builder.pubg.dto.PubgMatchResponse;
import com.party.builder.pubg.service.PubgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;

import java.util.List;

@RestController
@RequestMapping("/api/pubg")
@RequiredArgsConstructor
public class PubgController {

    private final PubgService pubgService;

    @GetMapping("/players/{nickname}")
    public ResponseEntity<PlayerSummaryResponse> getPlayer(
            @PathVariable String nickname
    ) {
        return ResponseEntity.ok(
                pubgService.getPlayerByNickname(nickname)
        );
    }

    @GetMapping("/matches/{matchId}")
    public ResponseEntity<PubgMatchResponse> getMatch(
            @PathVariable String matchId
    ) {
        return ResponseEntity.ok(
                pubgService.getMatch(matchId)
        );
    }

    @GetMapping("/matches/{matchId}/players/{accountId}")
    public ResponseEntity<PubgMatchResponse.Stats> getPlayerMatchStats(
            @PathVariable String matchId,
            @PathVariable String accountId
    ) {
        return ResponseEntity.ok(
                pubgService.getPlayerMatchStats(matchId, accountId)
        );
    }

    @GetMapping("/players/{nickname}/stats")
    public ResponseEntity<PlayerAggregateStats> getPlayerStats(
            @PathVariable String nickname
    ) {
        return ResponseEntity.ok(
                pubgService.analyzePlayer(nickname)
        );
    }

    @GetMapping("/players/{nickname}/style")
    public ResponseEntity<PlayerStyleProfile> getPlayerStyle(
            @PathVariable String nickname
    ) {
        return ResponseEntity.ok(
                pubgService.getPlayerStyle(nickname)
        );
    }

    @GetMapping("/matches/{matchId}/telemetry")
    public ResponseEntity<JsonNode[]> getTelemetry(
            @PathVariable String matchId
    ) {
        return ResponseEntity.ok(
                pubgService.getTelemetry(matchId)
        );
    }

    @GetMapping("/matches/{matchId}/players/{accountId}/events")
    public ResponseEntity<TelemetryFeatures> getPlayerEvents(
            @PathVariable String matchId,
            @PathVariable String accountId
    ) {

        return ResponseEntity.ok(
                pubgService.getPlayerTelemetryEvents(
                        matchId,
                        accountId
                )
        );
    }

    @GetMapping("/players/{nickname}/telemetry")
    public ResponseEntity<TelemetryAggregateStats> getPlayerTelemetry(
            @PathVariable String nickname
    ) {

        return ResponseEntity.ok(
                pubgService.analyzePlayerTelemetry(nickname)
        );
    }
}
