package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private Game testGame;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testGame = new Game();
        testGame.setId(1L);
        testGame.setName("TestGameName");
        testGame.setWithBots(true);

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
    }

    @Test
    public void testGetGames_success() {

        //gameRepository returns list
        List<Game> expectedList = Collections.singletonList(testGame);
        given(gameRepository.findAll()).willReturn(expectedList);

        List<Game> allGames = gameService.getGames();

        assertEquals(1, allGames.size(), "Element is not retrieved correctly");
        assertEquals(testGame, allGames.get(0), "Something went wrong retrieving the game");
    }

    @Test
    public void testCreateGame_success() {

        //setup
        given(gameRepository.saveAndFlush(testGame)).willReturn(testGame);
        given(gameRepository.findByName(testGame.getName())).willReturn(null);

        Game createdGame = gameService.createGame(testGame);

        assertEquals(testGame, createdGame, "The created game does not match the expected one!");
    }
}
