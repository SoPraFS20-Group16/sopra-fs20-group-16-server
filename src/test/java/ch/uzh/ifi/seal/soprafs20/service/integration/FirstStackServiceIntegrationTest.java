package ch.uzh.ifi.seal.soprafs20.service.integration;

import ch.uzh.ifi.seal.soprafs20.entity.game.FirstStack;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.repository.FirstStackRepository;
import ch.uzh.ifi.seal.soprafs20.service.FirstStackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebAppConfiguration
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
public class FirstStackServiceIntegrationTest {

    @Qualifier("firstStackRepository")
    @Autowired
    FirstStackRepository firstStackRepository;

    @Autowired
    FirstStackService firstStackService;

    private FirstStack stack;

    private Long first = 111L;
    private Long second = 222L;

    @BeforeEach
    public void setup() {
        firstStackRepository.deleteAll();
        firstStackRepository.flush();

        first = 111L;
        second = 222L;
        stack = new FirstStack();
        stack.setGameId(1L);
        stack = firstStackRepository.saveAndFlush(stack);
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
    public void testTeardown() {
        firstStackService.deleteStackForGame(stack.getGameId());

        List<FirstStack> stacks = firstStackRepository.findAll();

        //stacks should be empty
        assertEquals(0, stacks.size(), "Stacks should be empty");
    }
}
