package ch.uzh.ifi.seal.soprafs20.service.unit;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.PassMove;
import ch.uzh.ifi.seal.soprafs20.repository.MoveRepository;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.board.BoardService;
import ch.uzh.ifi.seal.soprafs20.service.board.TileService;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

public class MoveServiceTest {

    @InjectMocks
    private MoveService moveService;

    @Mock
    private MoveRepository moveRepository;

    @Mock
    private PlayerService playerService;

    @Mock
    private BoardService boardService;

    @Mock
    private TileService tileService;

    @Mock
    private GameService gameService;

    @Mock
    private MoveHandler testHandler;

    private Move testMove;

    private Game testGame;

    private Player testPlayer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(moveService, "playerService", playerService);
        ReflectionTestUtils.setField(moveService, "gameService", gameService);
        ReflectionTestUtils.setField(moveService, "boardService", boardService);
        ReflectionTestUtils.setField(moveService, "tileService", tileService);

        //Move with all default fields, returns testHandler
        testMove = new Move() {
            @Override
            public MoveHandler getMoveHandler() {
                return testHandler;
            }
        };

        testGame = new Game();
        testGame.setCurrentPlayer(new Player());
        testGame.setId(1L);

        testPlayer = new Player();
        testPlayer.setUserId(12L);
    }

    @Test
    public void testFindMoveById() {

        //setup
        Move move = new PassMove();
        move.setId(1L);

        given(moveRepository.findById(1L)).willReturn(Optional.of(move));

        assertEquals(move, moveService.findMoveById(1L), "The found move does not match the given Id!");
    }

    @Test
    public void testPerformMove() {
        //TODO: Find a way to test this method.
        // Should services always return their input or a boolean if operation is successful
        // and is not expected to return something else?
    }

    @Test
    public void testMakeRecalculations() {

        //Setup

        //Assumes the player has 1 victory point card
        given(playerService.getPointsFromDevelopmentCards(Mockito.any())).willReturn(1);

        //Assumes the player has 2 settlements or 1 city
        given(boardService.getPointsFromBuildings(Mockito.any(), Mockito.any())).willReturn(2);

        //new Moves empty, recalculation tested in tests for MoveCalculator
        given(testHandler.calculateNextMoves(Mockito.any(), Mockito.any())).willReturn(new ArrayList<>());

        moveService.makeRecalculations(testGame, testHandler, testMove);

        //Calculated points are
        assertEquals(3, testGame.getCurrentPlayer().getVictoryPoints(), "Victory point calculation wrong!");
    }
    //Perform moves are tested in integration tests!

}
