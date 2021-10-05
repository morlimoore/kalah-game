package com.ikenna.echendu.kalah.model;

import com.ikenna.echendu.kalah.entity.Game;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
//@AllArgsConstructor
//@Builder
public class GameRecord {

    {
        pots = new HashMap<>(){{
            put("1", 6);
            put("2", 6);
            put("3", 6);
            put("4", 6);
            put("5", 6);
            put("6", 6);
            put("7", 0);
            put("8", 6);
            put("9", 6);
            put("10", 6);
            put("11", 6);
            put("12", 6);
            put("13", 6);
            put("14", 0);
        }};
    }

    private String gameCode;
    private String creatorUsername;
    private String opponentUsername;
    private Map<String, Integer> pots;
    private String nextTurn;
    private String winner;
//    private int creatorMankalah;
//    private int opponentMankalah;
    private Enum.GameStatus status;

    public static GameRecord of (Game game) {
        GameRecord record = new GameRecord();
        record.setGameCode(game.getCode());
        record.setCreatorUsername(game.getCreatorUsername());
        record.setOpponentUsername(game.getOpponentUsername());
        record.setStatus(game.getStatus());
        record.setWinner(game.getWinner());
        return record;
    }


}
