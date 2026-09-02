package com.party.builder.pubg.service;

import com.party.builder.analysis.dto.PlayerStyleProfile;
import com.party.builder.analysis.dto.TelemetryAggregateStats;
import com.party.builder.analysis.dto.TelemetryFeatures;
import com.party.builder.analysis.service.PlayStyleAnalysisService;
import com.party.builder.analysis.service.TelemetryAnalysisService;
import com.party.builder.pubg.client.PubgMatchClient;
import com.party.builder.pubg.client.PubgPlayerClient;
import com.party.builder.pubg.client.PubgTelemetryClient;
import com.party.builder.pubg.dto.PlayerAggregateStats;
import com.party.builder.pubg.dto.PlayerSummaryResponse;
import com.party.builder.pubg.dto.PubgMatchResponse;
import com.party.builder.pubg.dto.PubgPlayerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PubgService {
    private final PubgPlayerClient pubgPlayerClient;
    private final PubgMatchClient pubgMatchClient;
    private final PubgTelemetryClient pubgTelemetryClient;
    private final PlayStyleAnalysisService playStyleAnalysisService;
    private final TelemetryAnalysisService telemetryAnalysisService;

    public PlayerSummaryResponse getPlayerByNickname(String nickname) {

        // response로 받아오기.
        PubgPlayerResponse response =
                pubgPlayerClient.getPlayerByNickname(nickname);

        if (response.getData() == null || response.getData().isEmpty()) {
            throw new IllegalArgumentException(
                    "플레이어를 찾을 수 없습니다: " + nickname
            );
        }

        // 플레이어 닉네임으로 한 명만 찾으니까 첫 번째 요소만 갖고 온다.
        PubgPlayerResponse.PlayerData player =
                response.getData().get(0);

        // 그 플레이어의 최근 매치 20판의 ID만을 추출한다.
        List<String> matchIds =
                player.getRelationships()
                        .getMatches()
                        .getData()
                        .stream()
                        .map(PubgPlayerResponse.MatchData::getId)
                        .toList();

        return PlayerSummaryResponse.builder()
                .accountId(player.getId())
                .nickname(player.getAttributes().getName())
                .shardId(player.getAttributes().getShardId())
                .matchIds(matchIds)
                .build();
    }

    public PubgMatchResponse getMatch(String matchId) {
        return pubgMatchClient.getMatch(matchId);
    }

    public PubgMatchResponse.Stats getPlayerMatchStats(
            String matchId,
            String accountId
    ) {

        PubgMatchResponse response =
                pubgMatchClient.getMatch(matchId);

        return response.getIncluded()
                .stream()
                .filter(included -> "participant".equals(included.getType()))
                .map(PubgMatchResponse.Included::getAttributes)
                .filter(attributes -> attributes != null)
                .map(PubgMatchResponse.ParticipantAttributes::getStats)
                .filter(stats -> stats != null)
                .filter(stats -> accountId.equals(stats.getPlayerId()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 경기에서 플레이어를 찾을 수 없습니다.")
                );
    }

    public List<PubgMatchResponse.Stats> getRecentPlayerStats(
            List<String> matchIds,
            String accountId
    ) {

        return matchIds.stream()
                .limit(5)
                .map(pubgMatchClient::getMatch)
                .filter(match -> !"airoyale".equals(
                        match.getData()
                                .getAttributes()
                                .getMatchType()
                ))
                .map(match -> match.getIncluded()
                        .stream()
                        .filter(included ->
                                "participant".equals(included.getType()))
                        .map(PubgMatchResponse.Included::getAttributes)
                        .filter(Objects::nonNull)
                        .map(PubgMatchResponse.ParticipantAttributes::getStats)
                        .filter(Objects::nonNull)
                        .filter(stats ->
                                accountId.equals(stats.getPlayerId()))
                        .peek(stats -> {
                            System.out.println("-------------------");
                            System.out.println("requested accountId: " + accountId);
                            System.out.println("playerId: " + stats.getPlayerId());
                            System.out.println("name: " + stats.getName());
                            System.out.println("kills: " + stats.getKills());
                        })
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    public PlayerAggregateStats analyzePlayer(String nickname) {

        PlayerSummaryResponse player =
                getPlayerByNickname(nickname);

        List<String> recentMatchIds = player.getMatchIds()
                .stream()
                .limit(5)
                .toList();

        List<PubgMatchResponse.Stats> statsList =
                getRecentPlayerStats(
                        recentMatchIds,
                        player.getAccountId()
                );

        return aggregateStats(statsList);
    }

    public PlayerAggregateStats aggregateStats(
            List<PubgMatchResponse.Stats> statsList
    ) {

        int count = statsList.size();

        if (count == 0) {
            throw new IllegalArgumentException("분석 가능한 경기 데이터가 없습니다.");
        }

        double avgKills = statsList.stream()
                .mapToInt(PubgMatchResponse.Stats::getKills)
                .average()
                .orElse(0);

        double avgDamage = statsList.stream()
                .mapToDouble(PubgMatchResponse.Stats::getDamageDealt)
                .average()
                .orElse(0);

        double avgDBNOs = statsList.stream()
                .mapToInt(PubgMatchResponse.Stats::getDBNOs)
                .average()
                .orElse(0);

        double avgAssists = statsList.stream()
                .mapToInt(PubgMatchResponse.Stats::getAssists)
                .average()
                .orElse(0);

        double avgRevives = statsList.stream()
                .mapToInt(PubgMatchResponse.Stats::getRevives)
                .average()
                .orElse(0);

        double avgSurvivalTime = statsList.stream()
                .mapToInt(PubgMatchResponse.Stats::getTimeSurvived)
                .average()
                .orElse(0);

        double avgWalkDistance = statsList.stream()
                .mapToDouble(PubgMatchResponse.Stats::getWalkDistance)
                .average()
                .orElse(0);

        double avgRideDistance = statsList.stream()
                .mapToDouble(PubgMatchResponse.Stats::getRideDistance)
                .average()
                .orElse(0);

        long top10Count = statsList.stream()
                .filter(stats -> stats.getWinPlace() <= 10)
                .count();

        double top10Rate = (double) top10Count / count;

        long survivedCount = statsList.stream()
                .filter(stats -> "alive".equals(stats.getDeathType()))
                .count();

        double survivalRate =
                (double) survivedCount / count;

        int totalKills = statsList.stream()
                .mapToInt(PubgMatchResponse.Stats::getKills)
                .sum();

        int totalHeadshotKills = statsList.stream()
                .mapToInt(PubgMatchResponse.Stats::getHeadshotKills)
                .sum();

        double headshotRate = totalKills == 0
                ? 0
                : (double) totalHeadshotKills / totalKills;

        return PlayerAggregateStats.builder()
                .matchesAnalyzed(count)
                .avgKills(avgKills)
                .avgDamage(avgDamage)
                .avgDBNOs(avgDBNOs)
                .avgAssists(avgAssists)
                .avgRevives(avgRevives)
                .avgSurvivalTime(avgSurvivalTime)
                .avgWalkDistance(avgWalkDistance)
                .avgRideDistance(avgRideDistance)
                .survivalRate(survivalRate)
                .top10Rate(top10Rate)
                .headshotRate(headshotRate)
                .build();
    }

    public JsonNode[] getTelemetry(String matchId) {

        String telemetryUrl =
                pubgMatchClient.getTelemetryUrl(matchId);

        return pubgTelemetryClient.getTelemetry(
                telemetryUrl
        );
    }

    public TelemetryFeatures getPlayerTelemetryEvents(
            String matchId,
            String accountId
    ) {

        String telemetryUrl =
                pubgMatchClient.getTelemetryUrl(matchId);

        JsonNode[] events =
                pubgTelemetryClient.getTelemetry(telemetryUrl);

        List<JsonNode> jsonNodeList = telemetryAnalysisService.extractPlayerEvents(
                events,
                accountId
        );

        return telemetryAnalysisService.extractFeatures(jsonNodeList);
    }

    public TelemetryAggregateStats analyzePlayerTelemetry(String nickname) {

        PlayerSummaryResponse player = getPlayerByNickname(nickname);

        List<String> recentMatchIds = player.getMatchIds()
                .stream()
                .limit(5)
                .toList();

        List<TelemetryFeatures> featuresList = recentMatchIds
                .stream()
                .map(pubgMatchClient::getMatch)
                .filter(match ->
                        !"airoyale".equals(
                                match.getData()
                                        .getAttributes()
                                        .getMatchType()
                        )
                )
                .map(match -> {

                    String matchId = match.getData().getId();

                    String telemetryUrl =
                            pubgMatchClient.getTelemetryUrl(matchId);

                    JsonNode[] events =
                            pubgTelemetryClient.getTelemetry(telemetryUrl);

                    List<JsonNode> playerEvents =
                            telemetryAnalysisService.extractPlayerEvents(
                                    events,
                                    player.getAccountId()
                            );

                    return telemetryAnalysisService.extractFeatures(
                            playerEvents
                    );
                })
                .toList();

        return telemetryAnalysisService.aggregateTelemetry(
                featuresList
        );
    }
}
