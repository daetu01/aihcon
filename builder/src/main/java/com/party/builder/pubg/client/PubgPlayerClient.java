package com.party.builder.pubg.client;

import com.party.builder.pubg.dto.PubgPlayerResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PubgPlayerClient {
    private final RestClient pubgRestClient;

    public PubgPlayerClient(
            @Qualifier("pubgRestClient") RestClient pubgRestClient
    ) {
        this.pubgRestClient = pubgRestClient;
    }

    public PubgPlayerResponse getPlayerByNickname(String nickname) {
        return pubgRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/shards/steam/players")
                        .queryParam("filter[playerNames]", nickname)
                        .build())
                .retrieve()
                .body(PubgPlayerResponse.class);
    }
}
