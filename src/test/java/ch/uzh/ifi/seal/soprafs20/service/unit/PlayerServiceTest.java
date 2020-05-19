package ch.uzh.ifi.seal.soprafs20.service.unit;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.ResourceWallet;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.moves.PurchaseMove;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

class PlayerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    //TestObjects
    private User testUser;
    private Long testUserId;
    private Long testGameId;
    private Player testPlayer;


    @BeforeEach
    void setup() {

        MockitoAnnotations.initMocks(this);

        testUserId = 12L;
        testGameId = 1L;

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
    void testCreatePlayerFromUserId_success() {

        given(userRepository.findUserById(Mockito.any())).willReturn(testUser);
        given(playerRepository.saveAndFlush(Mockito.any())).willReturn(testPlayer);

        Player createdPlayer = playerService.createPlayer(testUserId, testGameId);

        assertNotNull(createdPlayer, "The created player should not be null!");
        assertEquals(testUser.getId(), createdPlayer.getUserId(), "The userId and the users id should match!");
        assertEquals(testUser.getUsername(), createdPlayer.getUsername(), "The username does not match!");
    }

    @Test
    void testCreatePlayerFromUser_noUserWithThisId() {
        when(userRepository.findUserById(Mockito.any())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> playerService.createPlayer(1L, testGameId),
                "If the user does not exist an exception should be thrown!");
    }

    @Test
    void testAddDevelopmentCard_playerNull() {

        PurchaseMove purchaseMove = new PurchaseMove();
        purchaseMove.setUserId(testUserId);
        purchaseMove.setGameId(testGameId);

        given(playerRepository.findByUserId(testUserId)).willReturn(null);

        assertThrows(NullPointerException.class, () -> playerService.addDevelopmentCard(purchaseMove));

    }

    @Test
    void testUpdateResources() {

        testPlayer.setWallet(new ResourceWallet());

        Settlement settlement = new Settlement();
        settlement.setUserId(testUserId);
        settlement.setType(BuildingType.SETTLEMENT);

        List<Building> buildings = Collections.singletonList(settlement);

        playerService.updateResources(ResourceType.BRICK, buildings, testPlayer);

        assertEquals(settlement.getResourceDistributingAmount(),
                testPlayer.getWallet().getResourceAmount(ResourceType.BRICK),
                "The amount of Brick should match the resourceDistibutingAmount");
    }
}
