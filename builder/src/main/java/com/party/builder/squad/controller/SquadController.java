package com.party.builder.squad.controller;

import com.party.builder.analysis.dto.PlayerStyleProfile;
import com.party.builder.analysis.service.PlayerAnalysisFacade;
import com.party.builder.squad.dto.SquadMatchResponse;
import com.party.builder.squad.dto.SquadRequest;
import com.party.builder.squad.service.SquadMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/squads")
@RequiredArgsConstructor
public class SquadController {
    private final SquadMatchingService squadMatchingService;
    private final PlayerAnalysisFacade playerAnalysisFacade;

    @PostMapping("/analyze")
    public ResponseEntity<SquadMatchResponse> analyzeSquad(
            @RequestBody SquadRequest request
    ) {

        List<PlayerStyleProfile> profiles =
                request.getNicknames().stream()
                        .map(playerAnalysisFacade::analyzePlayerStyle)
                        .toList();

        SquadMatchResponse response =
                squadMatchingService.analyze(
                        request.getNicknames(),
                        profiles
                );

        return ResponseEntity.ok(response);
    }
}
