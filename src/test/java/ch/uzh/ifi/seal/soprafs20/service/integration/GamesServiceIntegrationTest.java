package ch.uzh.ifi.seal.soprafs20.service.integration;

import ch.uzh.ifi.seal.soprafs20.constant.GameConstants;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class GamesServiceIntegrationTest {

    @Autowired
    GameService gameService;

    @Autowired
    EntityManager entityManager;

    @Qualifier("gameRepository")
    @Autowired
    GameRepository gameRepository;

    @Qualifier("userRepository")
    @Autowired
    UserRepository userRepository;

    @Qualifier("playerRepository")
    @Autowired
    PlayerRepository playerRepository;

    @Qualifier("tileRepository")
    @Autowired
    TileRepository tileRepository;

    @Qualifier("boardRepository")
    @Autowired
    BoardRepository boardRepository;

    //Test Objects
    private Game testGame;
    private User testUser;
    private Player testPlayer;

    @BeforeEach
    void setup() {

        //delete from all repositories
        userRepository.deleteAll();
        gameRepository.deleteAll();
        boardRepository.deleteAll();
        tileRepository.deleteAll();

        testUser = new User();
        testUser.setToken("Token");
        testUser.setUsername("TheUsername");
        testUser.setStatus(UserStatus.ONLINE);
        testUser.setPassword("ThePassword");
        testUser = userRepository.saveAndFlush(testUser);
        assertNotNull(testUser, "The user should not be null!");

        testGame = new Game();
        testGame.setName("TestGameName");
        testGame.setWithBots(true);
        testGame.setCreatorId(testUser.getId());
        testGame = gameRepository.saveAndFlush(testGame);
        assertNotNull(testGame, "The testGame should not be null");

        testPlayer = new Player();
        testPlayer.setUserId(testUser.getId());
        testPlayer.setGameId(testGame.getId());
        testPlayer.setUsername(testUser.getUsername());

        testGame.addPlayer(testPlayer);
        testGame = gameRepository.saveAndFlush(testGame);
    }

    @AfterEach
    void teardown() {
        userRepository.deleteAll();
        gameRepository.deleteAll();
        boardRepository.deleteAll();
        tileRepository.deleteAll();

        playerRepository.deleteAll();
    }

    @Test
    void testCreateGame() {

        gameRepository.deleteAll();
        playerRepository.deleteAll();

        Game createdGame = gameService.createGame(testGame);

        assertNotNull(createdGame, "The created game should not be null!");

        assertEquals(1, createdGame.getPlayers().size(), "There should be one player!");

        Player createdPlayer = createdGame.getPlayers().get(0);

        assertEquals(testUser.getUsername(), createdPlayer.getUsername(),
                "The new Player should match the user");
    }

    @Test
    void testFindGame() {

        gameRepository.deleteAll();

        testGame = gameService.createGame(testGame);

        assertNotNull(testGame, "The game should not be null");


        Game foundById = new Game();
        foundById.setId(testGame.getId());
        foundById = gameService.findGame(foundById);

        assertNotNull(foundById, "There should exist a game!");
        assertEquals(testGame.getName(), foundById.getName(), "The name should match!");

        Game foundByName = new Game();
        foundByName.setName(testGame.getName());
        foundByName = gameService.findGame(foundByName);

        assertNotNull(foundByName, "There should exist a game!");
        assertEquals(testGame.getId(), foundByName.getId(), "The id should match!");
    }

    @Test
    void testGetGames() {

        gameRepository.deleteAll();

        Game createdGame = gameService.createGame(testGame);

        assertNotNull(createdGame, "A game should be created!");

        List<Game> foundGames = gameService.getGames();

        assertNotNull(foundGames, "There should be a list of game objects!");
        assertEquals(1, foundGames.size(), "There should be one game in the repository!");
        assertEquals(testGame.getName(), foundGames.get(0).getName(), "The names does not match!");
    }

    @Test
    void testTeardownGame() {
        gameRepository.deleteAll();

        Game createdGame = gameService.createGame(testGame);

        //Teardown
        gameService.teardownGameWithId(createdGame.getId());

        //games should be empty
        List<Game> games = gameRepository.findAll();
        assertEquals(0, games.size(), "Game should be deleted");

        //players should be empty
        List<Player> players = playerRepository.findAll();
        assertEquals(0, players.size(), "Players should be deleted");

        //board should be empty
        List<Board> boards = boardRepository.findAll();
        assertEquals(0, boards.size(), "Boards should be deleted");

        //tiles should be empty
        List<Tile> tiles = tileRepository.findAll();
        assertEquals(0, tiles.size(), "Tiles should be deleted");
    }

    @Test
    void testGetGameForUser_userHasGame() {

        Game game = gameService.findGameOfUser(testUser.getId());

        assertEquals(testGame.getId(), game.getId(), "The user should have a player in the game");
    }

    @Test
    void testGetGameForUser_userHasNoGame() {

        //Delete players
        testGame.setPlayers(new ArrayList<>());
        testGame = gameRepository.saveAndFlush(testGame);

        Game game = gameService.findGameOfUser(testUser.getId());

        assertNull(game, "The user should not be in a game");
    }

    @Test
    void testFillWithBots() {

        //Fill game and update testGame with current repository object
        gameService.fillWithBots(testGame);
        testGame = gameService.findGameById(testGame.getId());

        assertEquals(GameConstants.DEFAULT_PLAYER_MAX, testGame.getPlayers().size(),
                "The game should have the default player max amount");
        int bots = 0;

        for (Player player : testGame.getPlayers()) {
            if (player.isBot()) bots++;
        }

        assertEquals((GameConstants.DEFAULT_PLAYER_MAX - 1), bots, "All except one player are bots");
    }
}
