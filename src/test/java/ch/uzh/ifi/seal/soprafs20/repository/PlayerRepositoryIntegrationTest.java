package ch.uzh.ifi.seal.soprafs20.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class PlayerRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("playerRepository")
    @Autowired
    private PlayerRepository playerRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;


    @Test
    public void findByUsername_success() {
        //TODO Implement TestCase
    }

    @Test
    public void findByToken_success() {
        //TODO Implement TestCase
    }

    @Test
    public void deletePlayer_userMustRemain() {
        //TODO Implement TestCase
    }

    @Test
    public void createPlayer_noDuplicateUser() {
        //TODO Implement TestCase
    }
}
