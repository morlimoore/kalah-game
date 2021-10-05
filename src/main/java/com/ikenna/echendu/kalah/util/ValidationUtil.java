package com.ikenna.echendu.kalah.util;

import com.ikenna.echendu.kalah.entity.Game;
import com.ikenna.echendu.kalah.exception.ApiException;
import com.ikenna.echendu.kalah.model.Enum;
import com.ikenna.echendu.kalah.model.GameRecord;
import com.ikenna.echendu.kalah.repository.GameRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

import static com.ikenna.echendu.kalah.config.ContextConfig.getBean;
import static com.ikenna.echendu.kalah.util.AuthUtil.getLoggedInUsername;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class ValidationUtil {

    private static GameRepository gameRepository = getBean(GameRepository.class);

    public static void checkFieldErrors(BindingResult result) {
        if (result.hasErrors()) {
            Optional<Object> optionalObject = Optional.ofNullable(result.getFieldError());
            if (optionalObject.isPresent()) {
                Optional<String> optionalMessage = Optional.ofNullable(result.getFieldError().getDefaultMessage());
                if (optionalMessage.isPresent())
                    throw new ApiException(BAD_REQUEST, optionalMessage.get());
            }
        }
    }

    public static void validateGameCodeFormat(String gameCode) {
        if (!gameCode.matches("\\d{3}-\\d{3}-\\d{3}"))
            throw new ApiException(BAD_REQUEST, "Invalid game code. It must be in the format: '000-000-000'.");
    }

    public static void validatePitId(String pitId) {
        if (pitId.contains(" "))
            throw new ApiException(BAD_REQUEST, "Pit ID cannot contain spaces.");
        if (!StringUtils.isNumeric(pitId) || Integer.parseInt(pitId) < 1 || Integer.parseInt(pitId) > 14)
            throw new ApiException(BAD_REQUEST, "Pit ID can only be a valid number between 1 to 6 and 8 to 13.");
        if (Integer.parseInt(pitId) == 7 || Integer.parseInt(pitId) == 14)
            throw new ApiException(BAD_REQUEST, "You cannot play a mankallah.");
    }

    public static void validateUserNotJoinOwnGame(Game game, String username) {
        if (game.getCreatorUsername().equals(username))
            throw new ApiException(FORBIDDEN, "You cannot join your own game as an opponent.");
    }

    public static void validateUserNotHaveActiveGame(String loggedInUsername) {
        List<Game> onGoingCreatedGames = gameRepository.getOngoingGamesByCreatorUsername(loggedInUsername);
        if (!onGoingCreatedGames.isEmpty()) {
            String errorMessage = "";
            Optional<String> optional = Optional.ofNullable(onGoingCreatedGames.get(0).getOpponentUsername());
            if (optional.isPresent())
                errorMessage = String.format("Sorry, you and %s still have a game with code: '%s' running. " +
                                "Please, conclude it before starting or joining another.", optional.get(),
                        onGoingCreatedGames.get(0).getCode());
            else
                errorMessage = String.format("Sorry, conclude your game with code: '%s', " +
                        "before attempting to start or join another.", onGoingCreatedGames.get(0).getCode());
            throw new ApiException(FORBIDDEN, errorMessage);
        }

        List<Game> onGoingGamesAsOpponent = gameRepository.getOngoingGamesByOpponentUsername(loggedInUsername);
        if (!onGoingGamesAsOpponent.isEmpty())
            throw new ApiException(FORBIDDEN,
                    String.format("Sorry, you are already an opponent with %s on game with code: '%s'.",
                            onGoingGamesAsOpponent.get(0).getCreatorUsername(),
                            onGoingGamesAsOpponent.get(0).getCode()));
    }

    public static Game fetchAndValidateGameExist(String gameCode) {
        Optional<Game> optional = gameRepository.findByCode(gameCode);
        if (optional.isPresent())
            return optional.get();
        throw new ApiException(BAD_REQUEST, String.format("Sorry, game with code: %s does not exist.", gameCode));
    }

    public static void validateUserIsAParticipantInListOfGames(String username, String gameCode) {
        List<Game> gamesAsACreator = gameRepository.getGamesByCreatorUsernameAndCode(username, gameCode);
        List<Game> gamesAsAnOpponent = gameRepository.getGamesByOpponentUsernameAndCode(username, gameCode);

        if (gamesAsACreator.isEmpty() && gamesAsAnOpponent.isEmpty())
            throw new ApiException(FORBIDDEN, "Sorry, you are not a participant in this game.");
    }

    public static void validateUserIsAParticipantInSingleGame(Game game, String username) {
        if (!game.getCreatorUsername().equals(username) && !game.getOpponentUsername().equals(username))
            throw new ApiException(FORBIDDEN, "Sorry, you are not a participant in this game.");
    }

    public static void validateUserIsPlayingOwnPot(Game game, String username, String pitId) {
        if (game.getCreatorUsername().equals(username) && Integer.parseInt(pitId) > 6 ||
            game.getOpponentUsername().equals(username) && Integer.parseInt(pitId) < 8)
            throw new ApiException(FORBIDDEN, "You cannot play stones from your opponent's pot.");
    }

    public static void validateGameHasOpponentAndIsNotConcluded(Game game) {
        if (StringUtils.isBlank(game.getOpponentUsername()))
            throw new ApiException(BAD_REQUEST, "Sorry, you need an opponent to play against.");

        if (game.getStatus().equals(Enum.GameStatus.CONCLUDED))
            throw new ApiException(BAD_REQUEST, "Sorry, this game is already concluded.");
    }

    public static void validateUserHasPresentTurn(GameRecord gameRecord) {
        if (gameRecord.getNextTurn() != null) {
            String loggedInUsername = getLoggedInUsername();
            if (!gameRecord.getNextTurn().equals(loggedInUsername))
                throw new ApiException(FORBIDDEN, "Sorry, it is not your turn to play.");
        }
    }

    public static void validatePotIsNotEmpty(int stones) {
        if (stones < 1)
            throw new ApiException(FORBIDDEN, "Pot is empty, please play another.");
    }
}
