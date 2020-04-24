package ch.uzh.ifi.seal.soprafs20.service.unit;

import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.PlayerQueue;
import ch.uzh.ifi.seal.soprafs20.repository.QueueRepository;
import ch.uzh.ifi.seal.soprafs20.service.QueueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

public class QueueServiceTest {

    @Mock
    QueueRepository queueRepository;

    @InjectMocks
    QueueService queueService;

    PlayerQueue queue;

    private Long player1;
    private Long player2;
    private Long player3;
    private Long player4;

    private List<Player> allPlayerObjects;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        queue = new PlayerQueue();

        this.player1 = 111L;
        this.player2 = 222L;
        this.player3 = 333L;
        this.player4 = 444L;

        allPlayerObjects = new ArrayList<>();

        Player playerToBeAdded = new Player();
        playerToBeAdded.setUserId(player1);
        allPlayerObjects.add(playerToBeAdded);

        playerToBeAdded = new Player();
        playerToBeAdded.setUserId(player2);
        allPlayerObjects.add(playerToBeAdded);

        playerToBeAdded = new Player();
        playerToBeAdded.setUserId(player3);
        allPlayerObjects.add(playerToBeAdded);

        playerToBeAdded = new Player();
        playerToBeAdded.setUserId(player4);
        allPlayerObjects.add(playerToBeAdded);

        for (Player player: allPlayerObjects) {
            queue.addUserId(player.getUserId());
        }
    }

    @Test
    public void testGetNextForGame() {

        //mock the repo
        given(queueRepository.findByGameId(Mockito.any())).willReturn(queue);

        Long first = queueService.getNextForGame(1L);
        Long second = queueService.getNextForGame(1L);
        Long third = queueService.getNextForGame(1L);
        Long fourth = queueService.getNextForGame(1L);

        //After the fourth, the first should go again
        assertEquals(first, queueService.getNextForGame(1L), "The first should go two after the fourth!");

        //Then the second moves again
        assertEquals(second, queueService.getNextForGame(1L), "the second should go now!");

        //Then the third
        assertEquals(third, queueService.getNextForGame(1L), "the third should go now!");

        //Then the fourth
        assertEquals(fourth, queueService.getNextForGame(1L), "the fourth should go now!");

        //Then the first again
        assertEquals(first, queueService.getNextForGame(1L), "the first should go now!");

        //Then the second again
        assertEquals(second, queueService.getNextForGame(1L), "the second should go now!");
    }

    @Test
}
