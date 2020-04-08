package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    PlayerRepository playerRepository;

    @Mock
    PlayerService playerService;

    @Mock
    BoardService boardService;

    @InjectMocks
    private GameService gameService;

    private Game testGame;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testGame = new Game();
        testGame.setId(1L);
        testGame.setName("TestGameName");
        testGame.setWithBots(true);
        testGame.setCreatorId(12L);

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
    }

    @Test
    public void testGetGames_success() {

        //gameRepository returns list
        List<Game> expectedList = Collections.singletonList(testGame);
        given(gameRepository.findAll()).willReturn(expectedList);

        List<Game> allGames = gameService.getGames();

        assertEquals(1, allGames.size(), "Element is not retrieved correctly");
        assertEquals(testGame, allGames.get(0), "Something went wrong retrieving the game");
    }

    @Test
    public void testGetGames_gameEmpty() {

        //gameRepository returns list
        List<Game> expectedList = new ArrayList<>();
        given(gameRepository.findAll()).willReturn(expectedList);

        List<Game> allGames = gameService.getGames();

        assertEquals(0, allGames.size(), "The returned list should be empty");

    }

    @Test
    public void testCreateGame_success() {

        Player testPlayer = new Player();
        testPlayer.setUserId(1L);
        testPlayer.setUsername("Username");

        Board testBoard = new Board();

        //setup
        given(gameRepository.saveAndFlush(testGame)).willReturn(testGame);
        given(gameRepository.findByName(testGame.getName())).willReturn(null);
        given(playerService.createPlayerFromUserId(Mockito.any())).willReturn(testPlayer);
        given(boardService.createBoard()).willReturn(testBoard);

        Game createdGame = gameService.createGame(testGame);

        assertEquals(testGame, createdGame, "The created game does not match the expected one!");
    }

    @Test
    public void testCreateGame_conflict() {

        //setup
        given(gameRepository.findByName(testGame.getName())).willReturn(testGame);

        Game createdGame = gameService.createGame(testGame);

        assertNull(createdGame, "The created game should be null due to conflict");
    }

    @Test
    public void testFindGame_findById() {

        given(gameRepository.findById(Mockito.any())).willReturn(Optional.of(testGame));

        Game foundGame = gameService.findGame(testGame);

        assertEquals(testGame.getId(), foundGame.getId(), "Id of expected and found does not match!");
        assertEquals(testGame.getName(), foundGame.getName(), "Name of expected and found does not match!");
        assertEquals(testGame.isWithBots(), foundGame.isWithBots(), "Is with bots does not match!");

    }

    @Test
    public void testFindGame_findByName() {

        given(gameRepository.findByName(testGame.getName())).willReturn(testGame);
        testGame.setId(null);

        Game foundGame = gameService.findGame(testGame);

        assertEquals(testGame.getName(), foundGame.getName(), "Name of expected and found does not match!");
        assertEquals(testGame.isWithBots(), foundGame.isWithBots(), "Is with bots does not match!");

    }

    @Test
    public void testFindGame_noMatch() {

        given(gameRepository.findByName(testGame.getName())).willReturn(testGame);
        testGame.setId(null);
        testGame.setName(null);

        Game foundGame = gameService.findGame(testGame);

        assertNull(foundGame, "The foundGame should be null if no match exists");

    }

    @Test
    public void testUserCanAccessGame_canAccess() {
        User testUser = new User();
        testUser.setToken("TestToken");
        testUser.setId(1L);
        testUser.setUsername("TestName");

        Player testPlayer = new Player();
        testPlayer.setUserId(1L);
        testPlayer.setId(12L);
        testPlayer.setUsername("TestName");

        testGame.setPlayers(Collections.singletonList(testPlayer));

        given(playerRepository.findByUserId(Mockito.any())).willReturn(testPlayer);

        boolean result = gameService.userCanAccessGame(testUser, testGame);

        assertTrue(result, "The user should be able to access the game!");
    }

    @Test
    public void testUserCanAccessGame_canNotAccess() {
        User testUser = new User();
        testUser.setToken("TestToken");
        testUser.setId(1L);
        testUser.setUsername("TestName");

        given(playerRepository.findByUserId(Mockito.any())).willReturn(null);

        boolean result = gameService.userCanAccessGame(testUser, testGame);

        assertFalse(result, "The user should not be able to access the game!");

    }

}
