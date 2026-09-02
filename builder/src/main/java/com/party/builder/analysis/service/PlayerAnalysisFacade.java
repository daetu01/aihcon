package com.party.builder.analysis.service;

import com.party.builder.analysis.dto.PlayerStyleProfile;
import com.party.builder.analysis.dto.TelemetryAggregateStats;
import com.party.builder.pubg.dto.PlayerAggregateStats;
import com.party.builder.pubg.service.PubgService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerAnalysisFacade {

    private final PubgService pubgService;
    private final PlayStyleAnalysisService playStyleAnalysisService;

    @Cacheable(value = "playerStyle", key = "#nickname")
    public PlayerStyleProfile analyzePlayerStyle(String nickname) {

        PlayerAggregateStats basicStats =
                pubgService.analyzePlayer(nickname);

        TelemetryAggregateStats telemetryStats =
                pubgService.analyzePlayerTelemetry(nickname);

        return playStyleAnalysisService.analyze(
                basicStats,
                telemetryStats
        );
    }
}
