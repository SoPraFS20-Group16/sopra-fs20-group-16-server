package ch.uzh.ifi.seal.soprafs20.repository;


import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class PlayerRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("playerRepository")
    @Autowired
    private PlayerRepository playerRepository;

    private final Long testGameId = 123L;

    @AfterEach
    public void teardown() {
        playerRepository.deleteAll();
    }

    @Test
    public void findByUsername_success() {

        Player player = new Player();
        player.setUserId(1L);
        player.setUsername("TheUsername");
        player.setGameId(testGameId);

        entityManager.persistAndFlush(player);

        Player foundPlayer = playerRepository.findByUsername("TheUsername");

        assertEquals(player.getUserId(), foundPlayer.getUserId(), "The userId should match!");
        assertEquals(player.getUsername(), foundPlayer.getUsername(), "The username should match!");


    }

    @Test
    public void findByUserId() {

        Player player = new Player();
        player.setUserId(1L);
        player.setUsername("TheUsername");
        player.setGameId(testGameId);

        entityManager.persistAndFlush(player);

        Player foundPlayer = playerRepository.findByUserId(1L);

        assertEquals(player.getUserId(), foundPlayer.getUserId(), "The userId should match!");
        assertEquals(player.getUsername(), foundPlayer.getUsername(), "The username should match!");

    }
}
