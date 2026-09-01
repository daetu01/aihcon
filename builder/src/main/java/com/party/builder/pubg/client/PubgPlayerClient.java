package com.party.builder.pubg.client;

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

    public String getPlayerByNickname(String nickname) {
        return pubgRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/shards/steam/players")
                        .queryParam("filter[playerNames]", nickname)
                        .build())
                .retrieve()
                .body(String.class);
    }
}
