package com.party.builder.pubg.service;

import com.party.builder.pubg.client.PubgPlayerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PubgService {
    private final PubgPlayerClient pubgPlayerClient;

    public String getPlayerByNickname(String nickname) {
        return pubgPlayerClient.getPlayerByNickname(nickname);
    }
}
