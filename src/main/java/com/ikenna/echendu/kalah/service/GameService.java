package com.ikenna.echendu.kalah.service;

import com.ikenna.echendu.kalah.dto.response.GameCreationResponse;
import com.ikenna.echendu.kalah.dto.response.GameStatusResponse;
import com.ikenna.echendu.kalah.payload.ApiResponse;
import com.ikenna.echendu.kalah.payload.CreateResponse;
import org.springframework.http.ResponseEntity;

public interface GameService {
    ResponseEntity<ApiResponse<GameCreationResponse>> createGame();

    ResponseEntity<ApiResponse<CreateResponse.Response>> joinGame(String gameCode);

    ResponseEntity<ApiResponse<GameStatusResponse>> getGameStatus(String gameCode);
}
