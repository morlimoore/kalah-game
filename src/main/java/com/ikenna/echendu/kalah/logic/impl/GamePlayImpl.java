package com.ikenna.echendu.kalah.logic.impl;

import com.ikenna.echendu.kalah.dto.response.GameStatusResponse;
import com.ikenna.echendu.kalah.entity.Game;
import com.ikenna.echendu.kalah.entity.User;
import com.ikenna.echendu.kalah.logic.GamePlay;
import com.ikenna.echendu.kalah.model.Enum;
import com.ikenna.echendu.kalah.model.GameRecord;
import com.ikenna.echendu.kalah.repository.GameRepository;
import com.ikenna.echendu.kalah.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.ikenna.echendu.kalah.util.AuthUtil.getLoggedInUsername;
import static com.ikenna.echendu.kalah.util.ValidationUtil.validatePotIsNotEmpty;
import static com.ikenna.echendu.kalah.util.ValidationUtil.validateUserHasPresentTurn;

@Component
public class GamePlayImpl implements GamePlay {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    private Map<String, GameRecord> gameRecordMap = new HashMap<>();
    private String lastDroppedPotId = "";

    @Override
    public GameStatusResponse makeMove(Game game, String potId) {
        GameRecord gameRecord = init(game);
        validateUserHasPresentTurn(gameRecord);
        Map<String, Integer> pots = gameRecord.getPots();

        int stones = pickUpStonesFromPot(pots, potId);
        distributeStonesToPots(gameRecord, getNextPot(gameRecord, potId), stones);
        assignNextTurn(gameRecord);

        if (hasNoStonesLeft(gameRecord)) {
            returnPartnerStonesToMankalah(gameRecord);
            endGame(gameRecord);
        }
        gameRecordMap.put(game.getCode(), gameRecord);
        return GameStatusResponse.of(gameRecord);
    }

    @Override
    public GameRecord getGameRecord(Game game) {
        Optional<GameRecord> optional = Optional.ofNullable(gameRecordMap.get(game.getCode()));
        if (optional.isPresent())
            return optional.get();
        return GameRecord.of(game);
    }



    private GameRecord init(Game game) {
        GameRecord gameRecord;
        Optional<GameRecord> gameRecordOptional = Optional.ofNullable(gameRecordMap.get(game.getCode()));
        if (gameRecordOptional.isPresent())
            gameRecord = gameRecordOptional.get();
        else {
            game.setStatus(Enum.GameStatus.ONGOING);
            gameRepository.save(game);
            gameRecord = GameRecord.of(game);
            gameRecordMap.put(game.getCode(), gameRecord);
        }
        return gameRecord;
    }

    private GameRecord assignNextTurn(GameRecord gameRecord) {
        String loggedInUsername = getLoggedInUsername();

        if (didStoneEndInMankalah(gameRecord, lastDroppedPotId))
            gameRecord.setNextTurn(getLoggedInUsername());
        else {
            if (loggedInUsername.equals(gameRecord.getCreatorUsername()))
                gameRecord.setNextTurn(gameRecord.getOpponentUsername());
            else
                gameRecord.setNextTurn(gameRecord.getCreatorUsername());
        }
        return gameRecord;
    }

    private int pickUpStonesFromPot(Map<String, Integer> pots, String potId) {
        int stones = pots.get(potId);
        validatePotIsNotEmpty(stones);
        pots.put(potId, 0);
        return stones;
    }

    private void distributeStonesToPots(GameRecord gameRecord, String presentPotId, int stones) {
        Map<String, Integer> pots = gameRecord.getPots();
        while (stones > 0) {
            lastDroppedPotId = presentPotId;
            int stonesInPot = pots.get(presentPotId);
            if (stones == 1 && stonesInPot == 0 && didStoneEndInOwnPot(gameRecord, presentPotId)) {
                String oppositePotId = getOppositePot(gameRecord, presentPotId);
                int stonesInOppositePot = pots.get(oppositePotId);
                pots.put(oppositePotId, 0);
                addStonesToOwnMankalah(gameRecord, stonesInOppositePot + stonesInPot);
            }
            pots.put(presentPotId, ++stonesInPot);

            presentPotId = getNextPot(gameRecord, presentPotId);
            stones--;
        }
    }

    private void addStonesToOwnMankalah(GameRecord gameRecord, int stones) {
        if (gameRecord.getCreatorUsername().equals(getLoggedInUsername())) {
            int stonesInMankalah = gameRecord.getPots().get("7");
            gameRecord.getPots().put("7", stonesInMankalah + stones);
        } else {
            int stonesInMankalah = gameRecord.getPots().get("14");
            gameRecord.getPots().put("14", stonesInMankalah + stones);
        }
    }

    private boolean didStoneEndInMankalah(GameRecord gameRecord, String lastPotId) {
        String loggedInUsername = getLoggedInUsername();
        if (gameRecord.getCreatorUsername().equals(loggedInUsername))
            return lastPotId.equals("7");
        return lastPotId.equals("14");
    }

    private boolean didStoneEndInOwnPot(GameRecord gameRecord, String potId) {
        String loggedInUsername = getLoggedInUsername();
        if (gameRecord.getCreatorUsername().equals(loggedInUsername))
            return Integer.parseInt(potId) <= 6;
        return Integer.parseInt(potId) >= 8 && Integer.parseInt(potId) < 14;
    }

    private String getNextPot(GameRecord gameRecord, String presentPotId) {
        int potId = Integer.parseInt(presentPotId);
        potId++;
        if (potId > 14)
            potId = 1;
        String loggedInUsername = getLoggedInUsername();
        if (gameRecord.getCreatorUsername().equals(loggedInUsername)) {
            if (potId == 14)
                potId = 1;
        } else {
            if (potId == 7)
                potId = 8;
        }
        return Integer.toString(potId);
    }

    private String getOppositePot(GameRecord gameRecord, String presentPotId) {
        String res = "";
        for (int i = 0; i < 7; i++) {
            res = getNextPot(gameRecord, presentPotId);
            presentPotId = res;
        }
        return res;
    }

    private boolean hasNoStonesLeft(GameRecord gameRecord) {
        if (gameRecord.getCreatorUsername().equals(getLoggedInUsername()))
            return hasNoStonesInRangeOfPots(gameRecord.getPots(), "1", "6");
        else
            return hasNoStonesInRangeOfPots(gameRecord.getPots(), "8", "13");
    }

    private boolean hasNoStonesInRangeOfPots(Map<String, Integer> pots, String startPotId, String endPotId) {
        int count = 0;
        for (int i = Integer.parseInt(startPotId); i <= Integer.parseInt(endPotId); i++) {
            if (pots.get(Integer.toString(i)) == 0)
                count++;
        }
        return count == 6;
    }

    private void returnPartnerStonesToMankalah(GameRecord gameRecord) {
        if (gameRecord.getCreatorUsername().equals(getLoggedInUsername())) {
            int totalStones = mopUpStonesInRangeOfPots(gameRecord.getPots(), "1", "6");
            int stonesInMankalah = gameRecord.getPots().get("7");
            gameRecord.getPots().put("7", totalStones + stonesInMankalah);
        } else {
            int totalStones = mopUpStonesInRangeOfPots(gameRecord.getPots(), "8", "13");
            int stonesInMankalah = gameRecord.getPots().get("14");
            gameRecord.getPots().put("14", totalStones + stonesInMankalah);
        }
    }

    private int mopUpStonesInRangeOfPots(Map<String, Integer> pots, String startPotId, String endPotId) {
        int totalStones = 0;
        for (int i = Integer.parseInt(startPotId); i <= Integer.parseInt(endPotId); i++) {
            totalStones += pots.get(Integer.toString(i));
            pots.put(Integer.toString(i), 0);
        }
        return totalStones;
    }

    private void endGame(GameRecord gameRecord) {
        Optional<User> optionalCreator = userRepository.findByUsername(gameRecord.getCreatorUsername());
        Optional<User> optionalOpponent = userRepository.findByUsername(gameRecord.getOpponentUsername());
        Optional<Game> optionalGame = gameRepository.findByCode(gameRecord.getGameCode());

        if (optionalCreator.isPresent() && optionalOpponent.isPresent() && optionalGame.isPresent()) {
            User creator = optionalCreator.get();
            User opponent = optionalOpponent.get();
            Game game = optionalGame.get();

            game.setStatus(Enum.GameStatus.CONCLUDED);
            gameRecord.setStatus(Enum.GameStatus.CONCLUDED);
            gameRecord.setNextTurn("");

            if (gameRecord.getPots().get("7") > gameRecord.getPots().get("14")) {
                game.setWinner(gameRecord.getCreatorUsername());
                creator.setNumberOfWins(creator.getNumberOfWins() + 1);
                gameRecord.setWinner(gameRecord.getCreatorUsername());
            } else if (gameRecord.getPots().get("14") > gameRecord.getPots().get("7")) {
                game.setWinner(gameRecord.getOpponentUsername());
                opponent.setNumberOfWins(opponent.getNumberOfWins() + 1);
                gameRecord.setWinner(gameRecord.getOpponentUsername());
            } else
                game.setWinner("ITS A TIE");

            creator.setTotalGamesPlayed(creator.getTotalGamesPlayed() + 1);
            userRepository.save(creator);
            opponent.setTotalGamesPlayed(opponent.getTotalGamesPlayed() + 1);
            userRepository.save(opponent);
            gameRepository.save(game);
        }
    }
}