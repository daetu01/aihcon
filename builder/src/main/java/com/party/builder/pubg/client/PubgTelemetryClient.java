package com.party.builder.pubg.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

@Component
public class PubgTelemetryClient {

    private final RestClient restClient;

    public PubgTelemetryClient() {
        this.restClient = RestClient.create();
    }

    public JsonNode [] getTelemetry(String url) {

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(JsonNode[].class);
    }
}
