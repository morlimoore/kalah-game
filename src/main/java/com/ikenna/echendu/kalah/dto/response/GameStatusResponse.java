package com.ikenna.echendu.kalah.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ikenna.echendu.kalah.model.GameRecord;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GameStatusResponse {
    private String gameCode;
    private String url;
    private GamePot game;
    private String nextTurn;
    private String status;
    private String creatorUsername;
    private String opponentUsername;
    private String winner;

    @AllArgsConstructor
    @Data
    public static class GamePot {
        private Map<String, Integer> pots;
    }

    public static GameStatusResponse of (GameRecord gameRecord) {
        GameStatusResponse response = new GameStatusResponse();
        response.setGameCode(gameRecord.getGameCode());
        response.setUrl("http://localhost:8080/api/v1/kalah/" + gameRecord.getGameCode());
        response.setNextTurn(gameRecord.getNextTurn());
        response.setStatus(gameRecord.getStatus().toString());
        response.setCreatorUsername(gameRecord.getCreatorUsername());
        response.setOpponentUsername(gameRecord.getOpponentUsername());

        Map<String, Integer> pots = new TreeMap<>(Comparator.comparingInt(Integer::parseInt));
        pots.putAll(gameRecord.getPots());
        response.setGame(new GamePot(pots));
        return response;
    }
}
