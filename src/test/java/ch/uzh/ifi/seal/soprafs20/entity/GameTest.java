package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game testGame;
    private Player testPlayer;

    @BeforeEach
    public void setup() {

        testGame = new Game();
        testGame.setId(1L);
        testGame.setName("TestGame");
        testGame.setWithBots(false);

        testPlayer = new Player();
        testPlayer.setId(12L);
        testPlayer.setUserId(11L);
        testPlayer.setUsername("TestUser");
    }

    @Test
    public void testIsPlayer_isTrue() {
        testGame.addPlayer(testPlayer);

        assertTrue(testGame.isPlayer(testPlayer), "The player should be a player of the game!");
    }

    @Test
    public void testIsPlayer_isFalse() {

        assertFalse(testGame.isPlayer(testPlayer), "The player should not be in the game!");
    }

    @Test
    public void testAddPlayer_success() {
        int empty = testGame.getPlayers().size();
        assertEquals(0, empty, "Initially the player array should be empty!");

        testGame.addPlayer(testPlayer);

        int onePlayer = testGame.getPlayers().size();
        assertEquals(1, onePlayer, "The player array should hold one player!");

        Player foundPlayer = testGame.getPlayers().get(0);
        assertEquals(testPlayer.getId(), foundPlayer.getId(),
                "The player that was added does not match the player found!");
    }

    @Test
    public void testAddPlayer_passedNull() {

        Exception exception = assertThrows(NullPointerException.class, () -> testGame.addPlayer(null));

        String expected = "Game.addPlayer does not take null as input!";
        String result = exception.getMessage();

        assertEquals(expected, result, "The message does not match!");
    }

    @Test
    public void testAddPlayer_alreadyAdded() {
        testGame.addPlayer(testPlayer);
        assertEquals(1, testGame.getPlayers().size(), "Game should only have one player!");

        testGame.addPlayer(testPlayer);
        assertEquals(2, testGame.getPlayers().size(), "The game should still only have one player!");
    }

    @Test
    public void testGetPlayers_containsPlayer() {

        testGame.addPlayer(testPlayer);

        List<Player> expected = Collections.singletonList(testPlayer);

        List<Player> result = testGame.getPlayers();

        assertEquals(expected.size(), result.size(), "The returned array has unexpected size!");
        assertEquals(expected.get(0), result.get(0), "The user in the array is not the expected one!");

    }

    @Test
    public void testGetPlayers_isEmpty() {

        List<Player> result = testGame.getPlayers();

        assertEquals(0, result.size(), "The list should be empty!");

    }
}
