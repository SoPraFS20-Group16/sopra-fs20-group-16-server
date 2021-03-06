package ch.uzh.ifi.seal.soprafs20.service.integration;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@WebAppConfiguration
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class PlayerServiceIntegrationTest {

    private final Long testGameId = 321L;
    @Qualifier("coordinateRepository")
    @Autowired
    CoordinateRepository coordinateRepository;
    @Qualifier("tileRepository")
    @Autowired
    TileRepository tileRepository;
    @Qualifier("boardRepository")
    @Autowired
    BoardRepository boardRepository;
    @Qualifier("moveRepository")
    @Autowired
    MoveRepository moveRepository;
    @Qualifier("gameRepository")
    @Autowired
    GameRepository gameRepository;
    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;
    @Qualifier("playerRepository")
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private EntityManager entityManager;
    //Test Objects
    private User testUser;
    private Long testUserId;

    @BeforeEach
    void setup() {

        gameRepository.deleteAll();

        moveRepository.deleteAll();

        userRepository.deleteAll();
        playerRepository.deleteAll();

        boardRepository.deleteAll();
        tileRepository.deleteAll();

        coordinateRepository.deleteAll();

        testUser = new User();
        testUser.setToken("Token");
        testUser.setUsername("Username");
        testUser.setPassword("Password");
        testUser.setStatus(UserStatus.ONLINE);

        userRepository.deleteAll();
        testUser = userRepository.saveAndFlush(testUser);

        assertNotNull(testUser, "The testUser should not be null!");

        testUserId = testUser.getId();
    }

    @AfterEach
    void teardown() {
        gameRepository.deleteAll();

        moveRepository.deleteAll();

        userRepository.deleteAll();
        playerRepository.deleteAll();

        boardRepository.deleteAll();
        tileRepository.deleteAll();

        coordinateRepository.deleteAll();
    }

    @Test
    void testCreatePlayerFromUserId() {

        playerRepository.deleteAll();
        Player createdPlayer = playerService.createPlayer(testUserId, testGameId);

        assertNotNull(createdPlayer, "The created player should not be null!");
        assertEquals(testUser.getId(), createdPlayer.getUserId(), "The userId and the users id should match!");
        assertEquals(testUser.getUsername(), createdPlayer.getUsername(), "The username does not match!");
    }
}
