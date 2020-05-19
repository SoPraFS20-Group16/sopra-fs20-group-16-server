package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.entity.game.FirstStack;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FirstStackTest {

    private FirstStack stack;
    private Long player1;
    private Long player2;
    private Long player3;
    private Long player4;

    private List<Player> allPlayerObjects;

    @BeforeEach
    void setup() {
        this.stack = new FirstStack();

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

        stack.setup(allPlayerObjects);
    }


    @Test
    void testGetUserIdForPlayerAfter() {
        Long first = stack.getFirstPlayersUserId();
        Long second = stack.getNext();
        Long third = stack.getNext();
        Long fourth = stack.getNext();

        //After the fourth, the fourth moves again
        assertEquals(fourth, stack.getNext(), "The fourth should go two times!");

        //Then the third moves again
        assertEquals(third, stack.getNext(), "the third should go now!");

        //Then the second
        assertEquals(second, stack.getNext(), "the second should go now!");

        //Then the first
        assertEquals(first, stack.getNext(), "the first should go now!");

        //Then the first again
        assertEquals(first, stack.getNext(), "the first should go now!");

        //Then the second again
        assertEquals(second, stack.getNext(), "the second should go now!");
    }
}
