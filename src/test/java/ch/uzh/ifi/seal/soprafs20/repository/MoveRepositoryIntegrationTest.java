package ch.uzh.ifi.seal.soprafs20.repository;


import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MoveRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("moveRepository")
    @Autowired
    private MoveRepository moveRepository;

    @AfterEach
    public void teardown() {

        entityManager.clear();
    }

    @Test
    public void findById_success() {
        // given
        Move move = new BuildMove();
        move.setUserId(12L);


        //Id is set by automatically, if it is the first element, the id is 1

        Move persistedMove = entityManager.persist(move);
        entityManager.flush();


        Optional<Move> optionalFound = moveRepository.findById(persistedMove.getId());

        assertTrue(optionalFound.isPresent(), "The move is not present in the database!");
        Move found = optionalFound.get();


        // then
        assertNotNull(found.getId());
        assertEquals(found.getGameId(), move.getGameId());
        assertEquals(found.getUserId(), move.getUserId());
    }

}
