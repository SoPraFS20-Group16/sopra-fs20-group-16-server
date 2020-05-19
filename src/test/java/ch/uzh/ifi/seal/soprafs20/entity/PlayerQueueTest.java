package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.PlayerQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerQueueTest {

    private PlayerQueue queue;

    private Long player1;
    private Long player2;
    private Long player3;
    private Long player4;

    private List<Player> allPlayerObjects;

    @BeforeEach
    void setup() {

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
    void testGetNext() {

        Long first = queue.getNext();
        Long second = queue.getNext();
        Long third = queue.getNext();
        Long fourth = queue.getNext();

        //After the fourth, the first should go again
        assertEquals(first, queue.getNext(), "The first should go two after the fourth!");

        //Then the second moves again
        assertEquals(second, queue.getNext(), "the second should go now!");

        //Then the third
        assertEquals(third, queue.getNext(), "the third should go now!");

        //Then the fourth
        assertEquals(fourth, queue.getNext(), "the fourth should go now!");

        //Then the first again
        assertEquals(first, queue.getNext(), "the first should go now!");

        //Then the second again
        assertEquals(second, queue.getNext(), "the second should go now!");
    }
 }
