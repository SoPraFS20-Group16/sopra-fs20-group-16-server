package ch.uzh.ifi.seal.soprafs20.service.unit;

import ch.uzh.ifi.seal.soprafs20.constant.ApplicationConstants;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.service.BotService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

class BotServiceTest {

    private final Long testGameId = 123L;
    @InjectMocks
    private BotService botService;
    @Mock
    private PlayerService playerService;
    @Mock
    private MoveService moveService;
    @Mock
    private GameService gameService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(botService, "playerService", playerService);
        ReflectionTestUtils.setField(botService, "moveService", moveService);
        ReflectionTestUtils.setField(botService, "gameService", gameService);
    }

    /**
     * Tests the createBot() method of the BotService
     */
    @Test
    void testCreateBot() {

        given(playerService.save(Mockito.any(Player.class))).will(i -> i.getArguments()[0]);
        Player created = botService.createBot(testGameId);

        assertEquals(testGameId, created.getGameId(), "The gameId does not match");
        assertTrue(created.isBot(), "The player should be a bot");
        assertEquals(ApplicationConstants.BOT_NAME, created.getUsername(), "The bot name is wrong!");


    }
}
