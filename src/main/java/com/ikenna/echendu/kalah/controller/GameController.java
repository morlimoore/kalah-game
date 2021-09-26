package com.ikenna.echendu.kalah.controller;

import com.ikenna.echendu.kalah.dto.response.GameCreationResponse;
import com.ikenna.echendu.kalah.dto.response.GameStatusResponse;
import com.ikenna.echendu.kalah.exception.ApiException;
import com.ikenna.echendu.kalah.payload.ApiResponse;
import com.ikenna.echendu.kalah.payload.CreateResponse;
import com.ikenna.echendu.kalah.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
    public ResponseEntity<ApiResponse<CreateResponse.Success>> joinGame(@PathVariable String gameCode) {
        validateGameCodeFormat(gameCode);
        return gameService.joinGame(gameCode);
    }

    @GetMapping("/status/{gameCode}")
    public ResponseEntity<ApiResponse<GameStatusResponse>> getGameStatus(@PathVariable String gameCode) {
        validateGameCodeFormat(gameCode);
        return gameService.getGameStatus(gameCode);
    }



    private void validateGameCodeFormat(String gameCode) {
        if (!gameCode.matches("\\d{3}-\\d{3}-\\d{3}"))
            throw new ApiException(BAD_REQUEST, "Invalid game code. It must be in the format: 000-000-000");
    }
}
