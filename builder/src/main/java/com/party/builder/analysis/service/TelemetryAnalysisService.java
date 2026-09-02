package com.party.builder.analysis.service;

import com.party.builder.analysis.dto.TelemetryAggregateStats;
import com.party.builder.analysis.dto.TelemetryFeatures;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class TelemetryAnalysisService {
    private static final Set<String> TARGET_TYPES = Set.of(
            "LogPlayerAttack",
            "LogPlayerMakeGroggy",
            "LogPlayerKillV2",
            "LogPlayerRevive"
    );

    public List<JsonNode> extractPlayerEvents(
            JsonNode[] events,
            String accountId
    ) {

        return Arrays.stream(events)
                .filter(event -> {
                    JsonNode typeNode = event.get("_T");

                    return typeNode != null
                            && TARGET_TYPES.contains(typeNode.asText());
                })
                .filter(event -> containsAccountId(event, accountId))
                .toList();
    }

    private boolean containsAccountId(
            JsonNode event,
            String accountId
    ) {

        return event.toString().contains(accountId);
    }

    public TelemetryFeatures extractFeatures(List<JsonNode> events) {

        int attackCount = 0;
        int groggyCount = 0;
        int killCount = 0;
        int reviveCount = 0;

        for (JsonNode event : events) {

            String type = event.get("_T").asText();

            switch (type) {
                case "LogPlayerAttack" -> attackCount++;
                case "LogPlayerMakeGroggy" -> groggyCount++;
                case "LogPlayerKillV2" -> killCount++;
                case "LogPlayerRevive" -> reviveCount++;
            }
        }

        int engagementCount = calculateEngagementCount(events);

        return TelemetryFeatures.builder()
                .attackCount(attackCount)
                .engagementCount(engagementCount)
                .groggyCount(groggyCount)
                .killCount(killCount)
                .reviveCount(reviveCount)
                .build();
    }

    public int calculateEngagementCount(List<JsonNode> events) {

        List<Instant> attackTimes = events.stream()
                .filter(event ->
                        "LogPlayerAttack".equals(event.get("_T").asText()))
                .map(event ->
                        Instant.parse(event.get("_D").asText()))
                .sorted()
                .toList();

        if (attackTimes.isEmpty()) {
            return 0;
        }

        int engagementCount = 1;

        for (int i = 1; i < attackTimes.size(); i++) {

            long gapSeconds = Duration.between(
                    attackTimes.get(i - 1),
                    attackTimes.get(i)
            ).getSeconds();

            if (gapSeconds >= 10) {
                engagementCount++;
            }
        }

        return engagementCount;
    }

    public TelemetryAggregateStats aggregateTelemetry(
            List<TelemetryFeatures> featuresList
    ) {

        int count = featuresList.size();

        if (count == 0) {
            throw new IllegalArgumentException(
                    "분석 가능한 Telemetry 데이터가 없습니다."
            );
        }

        double avgAttackCount = featuresList.stream()
                .mapToInt(TelemetryFeatures::getAttackCount)
                .average()
                .orElse(0);

        double avgEngagementCount = featuresList.stream()
                .mapToInt(TelemetryFeatures::getEngagementCount)
                .average()
                .orElse(0);

        double avgGroggyCount = featuresList.stream()
                .mapToInt(TelemetryFeatures::getGroggyCount)
                .average()
                .orElse(0);

        double avgKillCount = featuresList.stream()
                .mapToInt(TelemetryFeatures::getKillCount)
                .average()
                .orElse(0);

        double avgReviveCount = featuresList.stream()
                .mapToInt(TelemetryFeatures::getReviveCount)
                .average()
                .orElse(0);

        return TelemetryAggregateStats.builder()
                .matchesAnalyzed(count)
                .avgAttackCount(avgAttackCount)
                .avgEngagementCount(avgEngagementCount)
                .avgGroggyCount(avgGroggyCount)
                .avgKillCount(avgKillCount)
                .avgReviveCount(avgReviveCount)
                .build();
    }
}
