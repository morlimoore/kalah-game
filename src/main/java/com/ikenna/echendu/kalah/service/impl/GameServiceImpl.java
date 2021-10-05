package com.ikenna.echendu.kalah.service.impl;

import com.ikenna.echendu.kalah.dto.response.GameCreationResponse;
import com.ikenna.echendu.kalah.dto.response.GameStatusResponse;
import com.ikenna.echendu.kalah.entity.Game;
import com.ikenna.echendu.kalah.logic.GamePlay;
import com.ikenna.echendu.kalah.model.Enum;
import com.ikenna.echendu.kalah.model.GameRecord;
import com.ikenna.echendu.kalah.payload.ApiResponse;
import com.ikenna.echendu.kalah.payload.CreateResponse;
import com.ikenna.echendu.kalah.repository.GameRepository;
import com.ikenna.echendu.kalah.service.GameService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.ikenna.echendu.kalah.payload.CreateResponse.successResponse;
import static com.ikenna.echendu.kalah.util.AuthUtil.getLoggedInUsername;
import static com.ikenna.echendu.kalah.util.GameUtil.getNumberSequence;
import static com.ikenna.echendu.kalah.util.ValidationUtil.*;
import static org.springframework.http.HttpStatus.*;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GamePlay gamePlay;

    @Override
    public ResponseEntity<ApiResponse<GameCreationResponse>> createGame() {
        String loggedInUsername = getLoggedInUsername();

        validateUserNotHaveActiveGame(loggedInUsername);

        GameCreationResponse response = new GameCreationResponse();

        Game game = buildNewGame(loggedInUsername);
        gameRepository.save(game);
        response.setGameCode(game.getCode());
        return successResponse(CREATED, response);
    }

    @Override
    public ResponseEntity<ApiResponse<CreateResponse.Response>> joinGame(String gameCode) {
        //Prevent user from joining his own game
        Game game = fetchAndValidateGameExist(gameCode);

        String loggedInUsername = getLoggedInUsername();

        validateUserNotJoinOwnGame(game, loggedInUsername);
        validateUserNotHaveActiveGame(getLoggedInUsername());

        game.setOpponentUsername(loggedInUsername);
        game.setStatus(Enum.GameStatus.READY);
        gameRepository.save(game);
        return successResponse(OK,
                String.format("You have successfully paired with %s on game with code: %s. Have fun!",
                        game.getCreatorUsername(), gameCode));
    }

    @Override
    public ResponseEntity<ApiResponse<GameStatusResponse>> getGameStatus(String gameCode) {
        Game game = fetchAndValidateGameExist(gameCode);

        String loggedInUsername = getLoggedInUsername();
        validateUserIsAParticipantInListOfGames(loggedInUsername, gameCode);

//        GameStatusResponse response = modelMapper.map(game, GameStatusResponse.class);
        GameStatusResponse response = GameStatusResponse.of(gamePlay.getGameRecord(game));
        return successResponse(OK, response);
    }

    @Override
    public ResponseEntity<ApiResponse<GameStatusResponse>> playGame(String gameCode, String potId) {
        Game game = fetchAndValidateGameExist(gameCode);
        validateGameHasOpponentAndIsNotConcluded(game);
        String loggedInUsername = getLoggedInUsername();
        validateUserIsAParticipantInSingleGame(game, loggedInUsername);
        validateUserIsPlayingOwnPot(game, loggedInUsername, potId);

        return successResponse(OK, gamePlay.makeMove(game, potId));
    }


    private Game buildNewGame(String username) {
        String code = getNumberSequence();
        while (gameRepository.existsByCode(code)) {
            code = getNumberSequence();
        }
        return Game.builder()
                .code(code)
                .creatorUsername(username)
                .status(Enum.GameStatus.CREATED)
                .build();
    }






}
