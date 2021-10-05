package com.ikenna.echendu.kalah.logic;

import com.ikenna.echendu.kalah.dto.response.GameStatusResponse;
import com.ikenna.echendu.kalah.entity.Game;
import com.ikenna.echendu.kalah.exception.ApiException;
import com.ikenna.echendu.kalah.model.GameRecord;

import java.util.Map;

public interface GamePlay {

    GameRecord init(Game game) throws ApiException;

    GameStatusResponse makeMove(Game game, String potId);

    String getNextTurn(String gameCode);

    Map<String, Integer> getPots(String gameCode);

    GameRecord getGameRecord(Game game);
}
