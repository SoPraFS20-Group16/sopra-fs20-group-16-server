package ch.uzh.ifi.seal.soprafs20.service.unit;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.FirstStack;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.repository.FirstStackRepository;
import ch.uzh.ifi.seal.soprafs20.service.FirstStackService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

public class FirstStackServiceTest {

    @Mock
    private FirstStackRepository stackRepository;

    @Mock
    private GameService gameService;

    @InjectMocks
    private FirstStackService firstStackService;

    private FirstStack stack;

    private Long first = 111L;
    private Long second = 222L;

    private Player player1;
    private Player player2;

    private List<Player> players;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(firstStackService, "gameService", gameService);

        first = 111L;
        second = 222L;
        stack = new FirstStack();

        player1 = new Player();
        player1.setUserId(first);

        player2 = new Player();
        player2.setUserId(second);

        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

    }

    @Test
    public void testGetNextForGame() {

        Player player1 = new Player();
        player1.setUserId(first);

        Player player2 = new Player();
        player2.setUserId(second);

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        stack.setup(players);

        //mock the stack repo
        given(stackRepository.findByGameId(Mockito.any())).willReturn(stack);

        //Asserts that "first" is the first player in the stack
        assertEquals(first, stack.getFirstPlayersUserId());

        //assert that the next is second
        assertEquals(second, firstStackService.getNextPlayerInGame(1L));

        //assert the second goes twice
        assertEquals(second, firstStackService.getNextPlayerInGame(1L));

        //assert the first comes again
        assertEquals(first, firstStackService.getNextPlayerInGame(1L));

        //assert the first goes twice as well
        assertEquals(first, firstStackService.getNextPlayerInGame(1L));

        //assert the second is next now
        assertEquals(second, firstStackService.getNextPlayerInGame(1L));
    }

    @Test
    public void testCreateStackForGame() {
        Game game = new Game();
        game.setId(1L);
        game.setPlayers(players);
        game.setCreatorId(player1.getUserId());

        given(gameService.findGameById(Mockito.anyLong())).willReturn(game);
        when(stackRepository.saveAndFlush(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        FirstStack result = firstStackService.createStackForGameWithId(game.getId());

        //Assert that stacks first player is the games current player
        assertEquals(game.getCurrentPlayer().getUserId(), result.getFirstPlayersUserId(),
                "The first should be set as current player");

        //The first player should be the creator
        assertEquals(game.getCreatorId(), result.getFirstPlayersUserId(),
                "The creator should be first");
    }
}
