package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game testGame;
    private Player testPlayer;

    @BeforeEach
    void setup() {

        testGame = new Game();
        testGame.setId(1L);
        testGame.setName("TestGame");
        testGame.setWithBots(false);

        testPlayer = new Player();
        testPlayer.setUserId(12L);
        testPlayer.setUserId(11L);
        testPlayer.setUsername("TestUser");
    }

    @Test
    void testIsPlayer_isTrue() {
        testGame.addPlayer(testPlayer);

        assertTrue(testGame.isPlayer(testPlayer), "The player should be a player of the game!");
    }

    @Test
    void testIsPlayer_isFalse() {

        assertFalse(testGame.isPlayer(testPlayer), "The player should not be in the game!");
    }

    @Test
    void testAddPlayer_success() {
        int empty = testGame.getPlayers().size();
        assertEquals(0, empty, "Initially the player array should be empty!");

        testGame.addPlayer(testPlayer);

        int onePlayer = testGame.getPlayers().size();
        assertEquals(1, onePlayer, "The player array should hold one player!");

        Player foundPlayer = testGame.getPlayers().get(0);
        assertEquals(testPlayer.getUserId(), foundPlayer.getUserId(),
                "The player that was added does not match the player found!");
    }

    /**
     * Tests that if null is passed a NullPointerException is thrown.
     */
    @Test
    void testAddPlayer_passedNull() {

        Exception exception = assertThrows(NullPointerException.class, () -> testGame.addPlayer(null));

        String expected = "Player to be added should not be null!";
        String result = exception.getMessage();

        assertEquals(0, testGame.getPlayers().size(), "The array should not contain elements");
        assertEquals(expected, result, "The message does not match!");
    }

    @Test
    void testAddPlayer_alreadyAdded() {
        testGame.addPlayer(testPlayer);
        assertEquals(1, testGame.getPlayers().size(), "Game should only have one player!");

        testGame.addPlayer(testPlayer);
        assertEquals(2, testGame.getPlayers().size(), "The game should still only have one player!");
    }

    @Test
    void testGetPlayers_containsPlayer() {

        testGame.addPlayer(testPlayer);

        List<Player> expected = Collections.singletonList(testPlayer);

        List<Player> result = testGame.getPlayers();

        assertEquals(expected.size(), result.size(), "The returned array has unexpected size!");
        assertEquals(expected.get(0), result.get(0), "The user in the array is not the expected one!");

    }

    @Test
    void testGetPlayers_isEmpty() {

        List<Player> result = testGame.getPlayers();

        assertEquals(0, result.size(), "The list should be empty!");

    }
}
