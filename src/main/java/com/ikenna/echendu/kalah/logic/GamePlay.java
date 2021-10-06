package com.ikenna.echendu.kalah.logic;

import com.ikenna.echendu.kalah.dto.response.GameStatusResponse;
import com.ikenna.echendu.kalah.entity.Game;
import com.ikenna.echendu.kalah.model.GameRecord;

public interface GamePlay {

    GameStatusResponse makeMove(Game game, String potId);

    GameRecord getGameRecord(Game game);
}
