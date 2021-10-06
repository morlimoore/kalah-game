package com.ikenna.echendu.kalah.controller;

import com.ikenna.echendu.kalah.dto.response.GameCreationResponse;
import com.ikenna.echendu.kalah.dto.response.GameStatusResponse;
import com.ikenna.echendu.kalah.entity.Game;
import com.ikenna.echendu.kalah.model.Enum;
import com.ikenna.echendu.kalah.model.GameRecord;
import com.ikenna.echendu.kalah.payload.ApiResponse;
import com.ikenna.echendu.kalah.payload.CreateResponse;
import com.ikenna.echendu.kalah.service.GameService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class GameControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private GameService gameService;

    private MockMvc mvc;

    @BeforeAll
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Can create game")
    void createGame() throws Exception {
        ApiResponse<GameCreationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(CREATED);
        apiResponse.setMessage("SUCCESS");
        apiResponse.setTime("2021-10-05 07:48:46 AM");
        apiResponse.setResult(new GameCreationResponse("123-456-789"));

        when(gameService.createGame()).thenReturn(ResponseEntity.status(CREATED).body(apiResponse));
        mvc.perform(post("/kalah")
        .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    @DisplayName("Can join game")
    void joinGame() throws Exception {
        ApiResponse<CreateResponse.Response> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(CREATED);
        apiResponse.setMessage("SUCCESS");
        apiResponse.setTime("2021-10-05 07:48:46 AM");
        apiResponse.setResult(new CreateResponse.Response("You have successfully paired with testUser on game with code: 123-456-789. Have fun!"));

        when(gameService.joinGame(anyString())).thenReturn(ResponseEntity.ok(apiResponse));
        mvc.perform(put("/kalah/join/123-456-789")
        .characterEncoding("utf-8")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    @DisplayName("Can get game status")
    void getGameStatus() throws Exception {
        Game game = Game.builder()
                .status(Enum.GameStatus.READY)
                .creatorUsername("testUser1")
                .opponentUsername("testUser2")
                .code("123-456-789")
                .build();
        GameRecord gameRecord = GameRecord.of(game);
        GameStatusResponse response = GameStatusResponse.of(gameRecord);
        ApiResponse<GameStatusResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(CREATED);
        apiResponse.setMessage("SUCCESS");
        apiResponse.setTime("2021-10-05 07:48:46 AM");
        apiResponse.setResult(response);

        when(gameService.getGameStatus(anyString())).thenReturn(ResponseEntity.ok(apiResponse));
        mvc.perform(get("/kalah/status/123-456-789")
        .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));

    }

    @Test
    @DisplayName("Can play game")
    void playGame() throws Exception {
        Game game = Game.builder()
                .status(Enum.GameStatus.ONGOING)
                .creatorUsername("testUser1")
                .opponentUsername("testUser2")
                .code("123-456-789")
                .build();
        GameRecord gameRecord = GameRecord.of(game);
        GameStatusResponse response = GameStatusResponse.of(gameRecord);
        ApiResponse<GameStatusResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(CREATED);
        apiResponse.setMessage("SUCCESS");
        apiResponse.setTime("2021-10-05 07:48:46 AM");
        apiResponse.setResult(response);

        when(gameService.playGame(anyString(), anyString())).thenReturn(ResponseEntity.ok(apiResponse));
        mvc.perform(put("/kalah/123-456-789/pits/1")
        .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }
}