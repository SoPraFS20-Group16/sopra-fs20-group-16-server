package ch.uzh.ifi.seal.soprafs20.service.integration;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.RestException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class UserServiceIntegrationTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @AfterEach
    void teardown() {

        userRepository.deleteAll();
        entityManager.clear();
    }

    @Test
    void createUser_validInputs_success() {
        // given
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("password");

        // when
        User createdUser = userService.createUser(testUser);

        // then
        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
    }

    @Test
    void createUser_duplicateUsername_throwsException() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("testUsername");
        User createdUser = userService.createUser(testUser);

        // attempt to create second user with same username
        User testUser2 = new User();
        testUser2.setUsername(createdUser.getUsername());
        testUser.setPassword("password");

        // check that an error is thrown
        assertThrows(RestException.class, () -> userService.createUser(testUser2));
    }
}
