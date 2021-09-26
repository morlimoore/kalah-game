package com.ikenna.echendu.kalah.service.impl;

import com.ikenna.echendu.kalah.dto.response.GameCreationResponse;
import com.ikenna.echendu.kalah.dto.response.GameStatusResponse;
import com.ikenna.echendu.kalah.entity.Game;
import com.ikenna.echendu.kalah.exception.ApiException;
import com.ikenna.echendu.kalah.payload.ApiResponse;
import com.ikenna.echendu.kalah.payload.CreateResponse;
import com.ikenna.echendu.kalah.repository.GameRepository;
import com.ikenna.echendu.kalah.service.GameService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ikenna.echendu.kalah.payload.CreateResponse.successResponse;
import static com.ikenna.echendu.kalah.util.AuthUtil.getLoggedInUsername;
import static com.ikenna.echendu.kalah.util.GameUtil.getNumberSequence;
import static org.springframework.http.HttpStatus.*;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<ApiResponse<GameCreationResponse>> createGame() {
        String loggedInUsername = getLoggedInUsername();

        validateUserNotHaveActiveGame(loggedInUsername);

        GameCreationResponse response = new GameCreationResponse();

        Game game = buildNewGame(loggedInUsername);
        gameRepository.save(game);
        response.setGameCode(game.getGameCode());
        return successResponse(CREATED, response);
    }

    @Override
    public ResponseEntity<ApiResponse<CreateResponse.Response>> joinGame(String gameCode) {
        validateGameExist(gameCode);

        String loggedInUsername = getLoggedInUsername();

        validateUserNotHaveActiveGame(getLoggedInUsername());

        Optional<Game> optionalGame = gameRepository.findByGameCode(gameCode);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            game.setOpponentUsername(loggedInUsername);
            gameRepository.save(game);
            return successResponse(OK,
                    String.format("You have successfully paired with %s on game with code: %s. Have fun!",
                            game.getCreatorUsername(), gameCode));
        } else
            throw new ApiException(INTERNAL_SERVER_ERROR, "Sorry, an error occurred with your request, try again.");
    }

    @Override
    public ResponseEntity<ApiResponse<GameStatusResponse>> getGameStatus(String gameCode) {
        validateGameExist(gameCode);

        String loggedInUsername = getLoggedInUsername();
        List<Game> gamesAsACreator = gameRepository.getGamesByCreatorUsernameAndGameCode(loggedInUsername, gameCode);
        List<Game> gamesAsAnOpponent = gameRepository.getGamesByOpponentUsernameAndGameCode(loggedInUsername, gameCode);

        if (gamesAsACreator.isEmpty() && gamesAsAnOpponent.isEmpty())
            throw new ApiException(FORBIDDEN, "Sorry, you are not a participant in this game.");

        Optional<Game> optionalGame = gameRepository.findByGameCode(gameCode);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            GameStatusResponse response = modelMapper.map(game, GameStatusResponse.class);
            return successResponse(OK, response);
        } else
            throw new ApiException(INTERNAL_SERVER_ERROR, "Sorry, an error occurred with your request, try again.");
    }


    private Game buildNewGame(String username) {
        String code = getNumberSequence();
        while (gameRepository.existsByGameCode(code)) {
            code = getNumberSequence();
        }
        return Game.builder()
                .gameCode(code)
                .creatorUsername(username)
                .build();
    }

    private void validateGameExist(String gameCode) {
        if (!gameRepository.existsByGameCode(gameCode))
            throw new ApiException(BAD_REQUEST, String.format("Sorry, game with code: %s does not exist.", gameCode));
    }

    private void validateUserNotHaveActiveGame(String loggedInUsername) {
        List<Game> onGoingCreatedGames = gameRepository.getOngoingGamesByCreatorUsername(loggedInUsername);
        if (!onGoingCreatedGames.isEmpty()) {
            String errorMessage = "";
            Optional<String> optional = Optional.ofNullable(onGoingCreatedGames.get(0).getOpponentUsername());
            if (optional.isPresent())
                errorMessage = String.format("Sorry, you and %s still have a game with code: '%s' running. " +
                                "Please, conclude it before starting or joining another.", optional.get(),
                        onGoingCreatedGames.get(0).getGameCode());
            else
                errorMessage = String.format("Sorry, conclude your game with code: '%s', " +
                        "before attempting to start or join another.", onGoingCreatedGames.get(0).getGameCode());
            throw new ApiException(FORBIDDEN, errorMessage);
        }

        List<Game> onGoingGamesAsOpponent = gameRepository.getOngoingGamesByOpponentUsername(loggedInUsername);
        if (!onGoingGamesAsOpponent.isEmpty())
            throw new ApiException(FORBIDDEN,
                    String.format("Sorry, you are already an opponent with %s on game with code: '%s'.",
                            onGoingGamesAsOpponent.get(0).getCreatorUsername(),
                            onGoingGamesAsOpponent.get(0).getGameCode()));
    }
}
