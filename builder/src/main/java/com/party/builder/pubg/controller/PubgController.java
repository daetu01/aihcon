package com.party.builder.pubg.controller;


import com.party.builder.pubg.service.PubgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pubg")
@RequiredArgsConstructor
public class PubgController {

    private final PubgService pubgService;

    @GetMapping("/players/{nickname}")
    public ResponseEntity<String> getPlayer(
            @PathVariable String nickname
    ) {
        return ResponseEntity.ok(
                pubgService.getPlayerByNickname(nickname)
        );
    }
}
