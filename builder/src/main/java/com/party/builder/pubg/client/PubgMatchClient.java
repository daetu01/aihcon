package com.party.builder.pubg.client;

import com.party.builder.pubg.dto.PubgMatchResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

@Component
public class PubgMatchClient {

    private final RestClient pubgRestClient;

    public PubgMatchClient(
            @Qualifier("pubgRestClient") RestClient pubgRestClient
    ) {
        this.pubgRestClient = pubgRestClient;
    }

    @Cacheable(value = "matches", key = "#matchId")
    public PubgMatchResponse getMatch(String matchId) {
        return pubgRestClient.get()
                .uri("/shards/steam/matches/{matchId}", matchId)
                .retrieve()
                .body(PubgMatchResponse.class);
    }

    public String getTelemetryUrl(String matchId) {

        JsonNode response = pubgRestClient.get()
                .uri("/shards/steam/matches/{matchId}", matchId)
                .retrieve()
                .body(JsonNode.class);

        if (response == null) {
            throw new IllegalStateException("Match 응답이 없습니다.");
        }

        JsonNode included = response.get("included");

        for (JsonNode node : included) {

            if ("asset".equals(node.get("type").asText())) {

                JsonNode attributes = node.get("attributes");

                if ("telemetry".equals(attributes.get("name").asText())) {
                    return attributes.get("URL").asText();
                }
            }
        }

        throw new IllegalArgumentException(
                "Telemetry URL을 찾을 수 없습니다."
        );
    }
}
