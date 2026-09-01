package com.party.builder.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient pubgRestClient(
            @Value("${pubg.api.base-url}") String baseUrl,
            @Value("${pubg.api.key}") String apiKey
    ) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(
                        "Authorization",
                        "Bearer " + apiKey
                )
                .defaultHeader(
                        "Accept",
                        "application/vnd.api+json"
                )
                .build();
    }
}
