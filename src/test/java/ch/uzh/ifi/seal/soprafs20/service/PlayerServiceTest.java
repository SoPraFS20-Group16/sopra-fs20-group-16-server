package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

public class PlayerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    //TestObjects
    private User testUser;
    private Long testUserId;
    private Player testPlayer;


    @BeforeEach
    public void setup() {

        MockitoAnnotations.initMocks(this);

        testUserId = 12L;

        testUser = new User();
        testUser.setToken("Token");
        testUser.setUsername("Username");
        testUser.setPassword("Password");
        testUser.setStatus(UserStatus.ONLINE);
        testUser.setId(testUserId);

        testPlayer = new Player();
        testPlayer.setUserId(testUserId);
        testPlayer.setUsername(testUser.getUsername());
    }

    @Test
    public void testCreatePlayerFromUserId_success() {

        given(userRepository.findUserById(Mockito.any())).willReturn(testUser);
        given(playerRepository.saveAndFlush(Mockito.any())).willReturn(testPlayer);

        Player createdPlayer = playerService.createPlayerFromUserId(testUserId);

        assertNotNull(createdPlayer, "The created player should not be null!");
        assertEquals(testUser.getId(), createdPlayer.getUserId(), "The userId and the users id should match!");
        assertEquals(testUser.getUsername(), createdPlayer.getUsername(), "The username does not match!");
    }

    @Test
    public void testCreatePlayerFromUser_noUserWithThisId() {
        when(userRepository.findUserById(Mockito.any())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> playerService.createPlayerFromUserId(1L),
                "If the user does not exist an exception should be thrown!");
    }
}
