package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebAppConfiguration
@SpringBootTest
public class GamesServiceIntegrationTest {

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

    @BeforeEach
    public void setup() {

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

    }

    @AfterEach
    public void teardown() {
        entityManager.clear();
    }

    @Test
    public void testCreateGame() {

        gameRepository.deleteAll();

        Game createdGame = gameService.createGame(testGame);

        assertNotNull(createdGame, "The created game should not be null!");

        assertEquals(1, createdGame.getPlayers().size(), "There should be one player!");

        Player createdPlayer = createdGame.getPlayers().get(0);

        assertEquals(testUser.getUsername(), createdPlayer.getUsername(),
                "The new Player should match the user");
    }

    @Test
    public void testFindGame() {

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
    public void testGetGames() {

        gameRepository.deleteAll();

        Game createdGame = gameService.createGame(testGame);

        assertNotNull(createdGame, "A game should be created!");

        List<Game> foundGames = gameService.getGames();

        assertNotNull(foundGames, "There should be a list of game objects!");
        assertEquals(1, foundGames.size(), "There should be one game in the repository!");
        assertEquals(testGame.getName(), foundGames.get(0).getName(), "The names does not match!");
    }
}
