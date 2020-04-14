package ch.uzh.ifi.seal.soprafs20.controller;


import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
public class GameControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    @MockBean
    private MoveService moveService;


    /**
     * Tests GET /games when games is empty
     *
     * @throws Exception the perform method can throw exceptions
     */
    @Test
    public void testGetGames_noGamesAvailable() throws Exception {
        // given
        List<Game> allGames = new ArrayList<>();

        // this mocks the GameService
        given(gameService.getGames()).willReturn(allGames);

        //This mocks the UserService for the token
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);
        given(userService.findUser(Mockito.any())).willReturn(user);


        // when
        MockHttpServletRequestBuilder getRequest = get("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", testToken);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * Tests GET /games when games exist
     *
     * @throws Exception the perform method can throw exceptions
     */
    @Test
    public void testGetGames_gamesAvailable() throws Exception {
        // given
        Game game = new Game();
        game.setId(1L);
        List<Game> allGames = Collections.singletonList(game);

        // this mocks the GameService
        given(gameService.getGames()).willReturn(allGames);

        //This mocks the UserService for the token
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);
        given(userService.findUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].gameId", is(1)))
                .andExpect(jsonPath("$[0].url", is("/games/1")));
    }

    /**
     * Tests GET /games when the token is incorrect.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetGames_tokenIncorrect() throws Exception {
        // given
        Game game = new Game();
        game.setId(1L);
        List<Game> allGames = Collections.singletonList(game);

        // this mocks the GameService
        given(gameService.getGames()).willReturn(allGames);

        //This mocks the UserService for the token
        String testToken = "ThisIsTheUserToken";
        given(userService.findUser(Mockito.any())).willReturn(null);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage", is("You are not logged in!")));
    }

    /**
     * Tests the POST /games endpoint.
     * Assumes Token is valid and creation of the game is successful
     *
     * @throws Exception the exception
     */
    @Test
    public void testPostGames_tokenValid_creationSuccessful() throws Exception {

        // given
        Game game = new Game();
        game.setId(1L);

        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setWithBots(false);
        gamePostDTO.setName("NewGameName");

        // this mocks the GameService
        given(gameService.createGame(Mockito.any())).willReturn(game);

        //This mocks the UserService for the token
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);
        given(userService.findUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", String.format("/games/%d", game.getId())));
    }


    /**
     * Tests the POST /games endpoint.
     * Assumes the token is not valid
     *
     * @throws Exception the exception
     */
    @Test
    public void testPostGames_tokenInvalid() throws Exception {

        // given
        Game game = new Game();
        game.setId(1L);

        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setWithBots(false);
        gamePostDTO.setName("NewGameName");

        // this mocks the GameService
        given(gameService.createGame(Mockito.any())).willReturn(null);

        //This mocks the UserService for the token
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);
        given(userService.findUser(Mockito.any())).willReturn(null);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage", is("You are not logged in!")));
    }

    /**
     * Tests the POST /games endpoint.
     * Assumes the token is not valid
     *
     * @throws Exception the exception
     */
    @Test
    public void testPostGames_gameCreationFails() throws Exception {

        // given
        Game game = new Game();
        game.setId(1L);

        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setWithBots(false);
        gamePostDTO.setName("NewGameName");

        // this mocks the GameService
        given(gameService.createGame(Mockito.any())).willReturn(null);

        //This mocks the UserService for the token
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);
        given(userService.findUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage",
                        is("There is already a game with this Name!")));
    }

    /**
     * Tests the GET /games/gameId endpoint.
     * Assumes all is correct
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetGameById_tokenValid_gameExists_userPermitted() throws Exception {

        // given
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);

        Game game = new Game();
        game.setId(1L);
        game.setName("GameName");
        game.setWithBots(false);

        // this mocks the GameService
        given(gameService.findGame(Mockito.any())).willReturn(game);
        given(gameService.userCanAccessGame(user, game)).willReturn(true);

        //this mocks the UserService
        given(userService.findUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games/1")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(game.getName())))
                .andExpect(jsonPath("$.gameId", is(game.getId().intValue())))
                .andExpect(jsonPath("$.withBots", is(game.isWithBots())));
    }

    /**
     * Tests the GET /games/gameId endpoint.
     * Assumes user is not allowed to access game
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetGameById_tokenValid_gameExists_userForbidden() throws Exception {

        // given
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);

        Game game = new Game();
        game.setId(1L);
        game.setName("GameName");
        game.setWithBots(false);

        // this mocks the GameService
        given(gameService.findGame(Mockito.any())).willReturn(game);
        given(gameService.userCanAccessGame(user, game)).willReturn(false);

        //this mocks the UserService
        given(userService.findUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games/1")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorMessage", is("Game access denied!")));
    }

    /**
     * Tests the GET /games/gameId endpoint.
     * Assumes game does not exist
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetGameById_tokenValid_gameNotExists() throws Exception {

        // given
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);

        Game game = new Game();
        game.setId(1L);
        game.setName("GameName");
        game.setWithBots(false);

        // this mocks the GameService
        given(gameService.findGame(Mockito.any())).willReturn(null);
        given(gameService.userCanAccessGame(user, game)).willReturn(false);

        //this mocks the UserService
        given(userService.findUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games/1")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage", is("This game does not exist!")));
    }

    /**
     * Tests the GET /games/gameId endpoint.
     * Token is not valid
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetGameById_tokenNotValid() throws Exception {

        // given
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);

        Game game = new Game();
        game.setId(1L);
        game.setName("GameName");
        game.setWithBots(false);

        // this mocks the GameService
        given(gameService.findGame(Mockito.any())).willReturn(game);
        given(gameService.userCanAccessGame(user, game)).willReturn(false);

        //this mocks the UserService
        given(userService.findUser(Mockito.any())).willReturn(null);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games/1")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage", is("You are not logged in!")));
    }

    /**
     * Tests the POST /games/gameId endpoint.
     * Assumes all is correct
     *
     * @throws Exception the exception
     */
    @Test
    public void testPostGameById_tokenValid_gameExists_moveExists_moveMatchesGame_userPermitted() throws Exception {

        // given
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);
        user.setId(12L);

        Move move = new BuildMove();
        move.setId(123L);
        move.setGameId(1L);
        move.setUserId(12L);

        MovePostDTO postDTO = new MovePostDTO();
        postDTO.setToken(testToken);
        postDTO.setMoveId(123L);

        Game game = new Game();
        game.setId(1L);
        game.setName("GameName");
        game.setWithBots(false);

        // this mocks the GameService
        given(gameService.findGame(Mockito.any())).willReturn(game);
        given(gameService.userCanAccessGame(user, game)).willReturn(true);

        //this mocks the MoveService
        given(moveService.findMoveById(Mockito.any())).willReturn(move);

        //this mocks the UserService
        given(userService.findUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games/1")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isAccepted());
    }

    /**
     * Tests the POST /games/gameId endpoint.
     * Assumes token is not valid
     *
     * @throws Exception the exception
     */
    @Test
    public void testPostGameById_tokenNotValid_gameExists_moveExists_moveMatchesGame() throws Exception {

        // given
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);
        user.setId(12L);

        Move move = new BuildMove();
        move.setId(123L);
        move.setGameId(1L);
        move.setUserId(12L);

        MovePostDTO postDTO = new MovePostDTO();
        postDTO.setToken(testToken);
        postDTO.setMoveId(123L);

        Game game = new Game();
        game.setId(1L);
        game.setName("GameName");
        game.setWithBots(false);

        // this mocks the GameService
        given(gameService.findGame(Mockito.any())).willReturn(game);
        given(gameService.userCanAccessGame(user, game)).willReturn(true);

        //this mocks the MoveService
        given(moveService.findMoveById(Mockito.any())).willReturn(move);

        //this mocks the UserService
        given(userService.findUser(Mockito.any())).willReturn(null);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games/1")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage", is("You are not logged in!")));
    }

    /**
     * Tests the POST /games/gameId endpoint.
     * Assumes the game posted to does not exist
     *
     * @throws Exception the exception
     */
    @Test
    public void testPostGameById_tokenValid_gameNotExists_moveExists_userPermitted() throws Exception {

        // given
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);
        user.setId(12L);

        Move move = new BuildMove();
        move.setId(123L);
        move.setGameId(1L);
        move.setUserId(12L);

        MovePostDTO postDTO = new MovePostDTO();
        postDTO.setToken(testToken);
        postDTO.setMoveId(123L);

        Game game = new Game();
        game.setId(1L);
        game.setName("GameName");
        game.setWithBots(false);

        // this mocks the GameService
        given(gameService.findGame(Mockito.any())).willReturn(null);
        given(gameService.userCanAccessGame(user, game)).willReturn(true);
        given(moveService.findMoveById(Mockito.any())).willReturn(move);


        //this mocks the UserService
        given(userService.findUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games/1")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage", is("This game does not exist!")));
    }

    /**
     * Tests the POST /games/gameId endpoint.
     * Assumes the move does not exist
     *
     * @throws Exception the exception
     */
    @Test
    public void testPostGameById_tokenValid_gameExists_moveNotExists_userPermitted() throws Exception {

        // given
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);
        user.setId(12L);

        Move move = new BuildMove();
        move.setId(123L);
        move.setGameId(1L);
        move.setUserId(12L);

        MovePostDTO postDTO = new MovePostDTO();
        postDTO.setToken(testToken);
        postDTO.setMoveId(123L);

        Game game = new Game();
        game.setId(1L);
        game.setName("GameName");
        game.setWithBots(false);

        // this mocks the GameService
        given(gameService.findGame(Mockito.any())).willReturn(game);
        given(gameService.userCanAccessGame(user, game)).willReturn(true);

        //this mocks the MoveService
        given(moveService.findMoveById(Mockito.any())).willReturn(null);

        //this mocks the UserService
        given(userService.findUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games/1")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorMessage", is("This is not a valid move!")));
    }

    /**
     * Tests the POST /games/gameId endpoint.
     * User is not permitted to access the game instance
     *
     * @throws Exception the exception
     */
    @Test
    public void testPostGameById_tokenValid_gameExists_moveExists_userNotPermitted() throws Exception {

        // given
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);
        user.setId(12L);

        Move move = new BuildMove();
        move.setId(123L);
        move.setGameId(1L);
        move.setUserId(12L);

        MovePostDTO postDTO = new MovePostDTO();
        postDTO.setToken(testToken);
        postDTO.setMoveId(123L);

        Game game = new Game();
        game.setId(1L);
        game.setName("GameName");
        game.setWithBots(false);

        // this mocks the GameService
        given(gameService.findGame(Mockito.any())).willReturn(game);
        given(gameService.userCanAccessGame(user, game)).willReturn(false);

        //this mocks the MoveService
        given(moveService.findMoveById(Mockito.any())).willReturn(move);

        //this mocks the UserService
        given(userService.findUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games/1")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorMessage", is("Game access denied!")));
    }

    /**
     * Tests the POST /games/gameId endpoint.
     * Moves gameId does not match games Id
     *
     * @throws Exception the exception
     */
    @Test
    public void testPostGameById__gameIdDoesntMatchMoveGameId() throws Exception {

        // given
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);
        user.setId(12L);

        Move move = new BuildMove();
        move.setId(123L);
        move.setGameId(2L);
        move.setUserId(12L);

        MovePostDTO postDTO = new MovePostDTO();
        postDTO.setToken(testToken);
        postDTO.setMoveId(123L);

        Game game = new Game();
        game.setId(1L);
        game.setName("GameName");
        game.setWithBots(false);

        // this mocks the GameService
        given(gameService.findGame(Mockito.any())).willReturn(game);
        given(gameService.userCanAccessGame(user, game)).willReturn(true);

        //this mocks the MoveService
        given(moveService.findMoveById(Mockito.any())).willReturn(move);

        //this mocks the UserService
        given(userService.findUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games/1")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorMessage", is("This is not a valid move!")));
    }

    /**
     * Tests the POST /games/gameId endpoint.
     * Users id does not match moves UserId
     *
     * @throws Exception the exception
     */
    @Test
    public void testPostGameById__playerIdDoesntMatchMovesPlayerId() throws Exception {

        // given
        String testToken = "ThisIsTheUserToken";
        User user = new User();
        user.setToken(testToken);
        user.setId(12L);

        Move move = new BuildMove();
        move.setId(123L);
        move.setGameId(1L);
        move.setUserId(22L);

        MovePostDTO postDTO = new MovePostDTO();
        postDTO.setToken(testToken);
        postDTO.setMoveId(123L);

        Game game = new Game();
        game.setId(1L);
        game.setName("GameName");
        game.setWithBots(false);

        // this mocks the GameService
        given(gameService.findGame(Mockito.any())).willReturn(game);
        given(gameService.userCanAccessGame(user, game)).willReturn(true);

        //this mocks the MoveService
        given(moveService.findMoveById(Mockito.any())).willReturn(move);

        //this mocks the UserService
        given(userService.findUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games/1")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorMessage", is("You are not allowed to make this move!")));
    }


    /**
     * Helper Method to convert DTOs into a JSON string such that the input can be processed
     *
     * @param object the object to be returned as json
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString())
            );
        }
    }

}
