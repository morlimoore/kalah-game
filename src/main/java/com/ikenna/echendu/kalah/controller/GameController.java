package com.ikenna.echendu.kalah.controller;

import com.ikenna.echendu.kalah.dto.response.GameCreationResponse;
import com.ikenna.echendu.kalah.dto.response.GameStatusResponse;
import com.ikenna.echendu.kalah.payload.ApiResponse;
import com.ikenna.echendu.kalah.payload.CreateResponse;
import com.ikenna.echendu.kalah.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ikenna.echendu.kalah.util.ValidationUtil.validateGameCodeFormat;
import static com.ikenna.echendu.kalah.util.ValidationUtil.validatePitId;

@RestController
@RequestMapping("/kalah")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping
    public ResponseEntity<ApiResponse<GameCreationResponse>> createGame() {
        return gameService.createGame();
    }

    @PutMapping("/join/{gameCode}")
    public ResponseEntity<ApiResponse<CreateResponse.Response>> joinGame(@PathVariable String gameCode) {
        validateGameCodeFormat(gameCode);
        return gameService.joinGame(gameCode);
    }

    @GetMapping("/status/{gameCode}")
    public ResponseEntity<ApiResponse<GameStatusResponse>> getGameStatus(@PathVariable String gameCode) {
        validateGameCodeFormat(gameCode);
        return gameService.getGameStatus(gameCode);
    }

    @PutMapping("/{gameCode}/pits/{pitId}")
    public ResponseEntity<ApiResponse<GameStatusResponse>> playGame(@PathVariable String gameCode, @PathVariable String pitId) {
        validateGameCodeFormat(gameCode);
        validatePitId(pitId);
        return gameService.playGame(gameCode, pitId);
    }

}
