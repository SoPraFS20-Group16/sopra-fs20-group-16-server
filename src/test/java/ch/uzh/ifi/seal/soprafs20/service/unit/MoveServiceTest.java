package ch.uzh.ifi.seal.soprafs20.service.unit;

import ch.uzh.ifi.seal.soprafs20.constant.GameConstants;
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

class MoveServiceTest {

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
    void setup() {
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
    void testFindMoveById() {

        //setup
        Move move = new PassMove();
        move.setId(1L);

        given(moveRepository.findById(1L)).willReturn(Optional.of(move));

        assertEquals(move, moveService.findMoveById(1L), "The found move does not match the given Id!");
    }

    @Test
    void testMakeRecalculations_belowWin() {

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

    @Test
    void testMakeRecalculations_aboveWin() {

        //Setup

        //Assumes the player has 1 victory point card
        given(playerService.getPointsFromDevelopmentCards(Mockito.any())).willReturn(1);

        //Assumes the player has 2 settlements or 1 city
        given(boardService.getPointsFromBuildings(Mockito.any(), Mockito.any())).willReturn(GameConstants.WIN_POINTS);

        //new Moves empty, recalculation tested in tests for MoveCalculator
        given(testHandler.calculateNextMoves(Mockito.any(), Mockito.any())).willReturn(new ArrayList<>());

        moveService.makeRecalculations(testGame, testHandler, testMove);

        //Calculated points are
        assertEquals(GameConstants.WIN_POINTS + 1, testGame.getCurrentPlayer().getVictoryPoints(), "Victory point calculation wrong!");
    }


    //Perform moves are tested in integration tests!

}
