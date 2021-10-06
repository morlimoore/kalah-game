package com.ikenna.echendu.kalah.model;

import com.ikenna.echendu.kalah.entity.Game;
import lombok.*;

import java.util.Map;
import java.util.TreeMap;

@Data
public class GameRecord {

    private String gameCode;
    private String creatorUsername;
    private String opponentUsername;
    private Map<String, Integer> pots = new TreeMap<>();
    private String nextTurn;
    private String winner;
    private Enum.GameStatus status;

    private GameRecord() {
        pots.put("1", 6);
        pots.put("2", 6);
        pots.put("3", 6);
        pots.put("4", 6);
        pots.put("5", 6);
        pots.put("6", 6);
        pots.put("7", 0);
        pots.put("8", 6);
        pots.put("9", 6);
        pots.put("10", 6);
        pots.put("11", 6);
        pots.put("12", 6);
        pots.put("13", 6);
        pots.put("14", 0);
    }

    public static GameRecord of (Game game) {
        GameRecord gameRecord = new GameRecord();
        gameRecord.setGameCode(game.getCode());
        gameRecord.setCreatorUsername(game.getCreatorUsername());
        gameRecord.setOpponentUsername(game.getOpponentUsername());
        gameRecord.setStatus(game.getStatus());
        gameRecord.setWinner(game.getWinner());
        return gameRecord;
    }


}
