package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class GameRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Qualifier("playerRepository")
    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void findById_success() {
        // given
        Game game = new Game();
        game.setName("GameName");
        game.setWithBots(false);
        game.setCreatorId(1L);

        //Id is set by automatically, if it is the first element, the id is 1
        Game savedGame = entityManager.persistAndFlush(game);

        Optional<Game> optionalFound = gameRepository.findById(savedGame.getId());

        assertTrue(optionalFound.isPresent(), "The game was not found in the database!");
        Game found = optionalFound.get();


        // then
        assertEquals(game.getName(), found.getName());
        assertEquals(game.isWithBots(), found.isWithBots());
    }

    @Test
    public void findByName_success() {
        // given
        Game game = new Game();
        game.setName("GameName");
        game.setWithBots(false);
        game.setCreatorId(1L);

        entityManager.persist(game);
        entityManager.flush();

        Game found = gameRepository.findByName(game.getName());

        // then
        assertNotNull(found.getId());
        assertEquals(game.getName(), found.getName());
        assertEquals(game.isWithBots(), found.isWithBots());
    }

    @Test
    public void findByName_noGameWithThatName() {
        // given
        Game game = new Game();
        game.setName("GameName");
        game.setWithBots(false);
        game.setCreatorId(1L);


        entityManager.persist(game);
        entityManager.flush();

        Game found = gameRepository.findByName("NotGameName");


        // then
        assertNull(found);
    }

    @Test
    public void findByName_nameNotSet() {
        // given
        Game game = new Game();
        game.setWithBots(false);
        game.setCreatorId(1L);

        Game found = gameRepository.findByName("NotGameName");


        // then
        assertNull(found);
    }

    @Test
    public void findByName_givenNull() {
        // given
        Game game = new Game();
        game.setWithBots(false);
        game.setName("TestGame");
        game.setCreatorId(1L);

        // given
        entityManager.persist(game);
        entityManager.flush();

        Game found = gameRepository.findByName(null);


        // then
        assertNull(found);
    }


    @Test
    public void testGetAll_success() {
        //given
        Game game = new Game();
        game.setName("TestGame");
        game.setWithBots(false);
        game.setCreatorId(1L);
        entityManager.persist(game);
        entityManager.flush();

        List<Game> foundList = gameRepository.findAll();

        assertEquals(1, foundList.size(), "The lists size should be 1");
        assertEquals(game, foundList.get(0));
    }

    @Test
    public void testGetAll_empty() {
        //given
        List<Game> foundList = gameRepository.findAll();

        assertNotNull(foundList, "The found list should be an empty array");
        assertEquals(0, foundList.size(), "The lists size should be 1");

    }

    @Test
    public void persistGameWithPlayer_userAlreadyExists() {

        Player player = new Player();
        player.setUsername("ThePlayer");
        player.setUserId(1L);

        //save player
        player = playerRepository.saveAndFlush(player);

        Game testGame = new Game();
        testGame.setWithBots(false);
        testGame.setName("TestGame");
        testGame.addPlayer(player);
        testGame.setCreatorId(1L);

        //Save the game
        entityManager.persist(testGame);
        entityManager.flush();

        Game resultGame = gameRepository.findByName("TestGame");
        assertEquals(testGame.getPlayers().size(), resultGame.getPlayers().size(),
                "The array size does not match!");

        Player resultPlayer = resultGame.getPlayers().get(0);

        //Check if the user still has all fields
        assertEquals(player.getId(), resultPlayer.getId(), "The id does not match!");
        assertEquals(player.getUsername(), resultPlayer.getUsername(), "The username does not match");
    }
}
