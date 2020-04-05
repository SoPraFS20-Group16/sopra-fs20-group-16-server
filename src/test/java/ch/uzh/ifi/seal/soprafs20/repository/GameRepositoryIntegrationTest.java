package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class GameRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Test
    public void findById_success() {
        // given
        Game game = new Game();
        game.setName("GameName");
        game.setWithBots(false);

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

        //Id is set by automatically, if it is the first element, the id is 1

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

        //Id is set by automatically, if it is the first element, the id is 1

        entityManager.persist(game);
        entityManager.flush();

        Game found = gameRepository.findByName("NotGameName");


        // then
        assertNull(found);
    }
}
