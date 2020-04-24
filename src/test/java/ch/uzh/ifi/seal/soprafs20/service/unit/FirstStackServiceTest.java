package ch.uzh.ifi.seal.soprafs20.service.unit;

import ch.uzh.ifi.seal.soprafs20.entity.game.FirstStack;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.repository.FirstStackRepository;
import ch.uzh.ifi.seal.soprafs20.service.FirstStackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

public class FirstStackServiceTest {

    @Mock
    private FirstStackRepository stackRepository;

    @InjectMocks
    private FirstStackService firstStackService;

    private FirstStack stack;

    private Long first = 111L;
    private Long second = 222L;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        first = 111L;
        second = 222L;
        stack = new FirstStack();
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
    }

    @Test
    public void testCanExitForGame() {


    }
}
