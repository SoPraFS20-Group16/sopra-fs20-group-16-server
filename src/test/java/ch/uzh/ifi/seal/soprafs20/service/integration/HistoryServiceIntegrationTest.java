package ch.uzh.ifi.seal.soprafs20.service.integration;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.PlayerQueue;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.history.BuildMoveHistory;
import ch.uzh.ifi.seal.soprafs20.entity.history.DiceMoveHistory;
import ch.uzh.ifi.seal.soprafs20.entity.history.GameHistory;
import ch.uzh.ifi.seal.soprafs20.entity.history.MoveHistory;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.DiceMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.PassMove;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import ch.uzh.ifi.seal.soprafs20.service.*;
import ch.uzh.ifi.seal.soprafs20.service.board.BoardService;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@WebAppConfiguration
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class HistoryServiceIntegrationTest {

    @Autowired
    FirstStackService firstStackService;

    @Qualifier("firstStackRepository")
    @Autowired
    FirstStackRepository firstStackRepository;

    @Qualifier("tileRepository")
    @Autowired
    TileRepository tileRepository;

    @Autowired
    BoardService boardService;

    @Autowired
    HistoryService historyService;

    @Qualifier("boardRepository")
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    QueueService queueService;

    @Qualifier("queueRepository")
    @Autowired
    QueueRepository queueRepository;
    @Autowired
    PlayerService playerService;
    @Qualifier("playerRepository")
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    private MoveService moveService;
    @Qualifier("moveRepository")
    @Autowired
    private MoveRepository moveRepository;
    @Autowired
    private GameService gameService;
    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;
    @Qualifier("resourceWalletRepository")
    @Autowired
    private ResourceWalletRepository resourceWalletRepository;
    private Move testMove;
    private Game testGame;
    private Player testPlayer;
    private PlayerQueue testQueue;
    private Board testBoard;

    @BeforeEach
    void setup() {

        //Delete Wallets before players
        resourceWalletRepository.deleteAll();

        //Delete tiles before board
        tileRepository.deleteAll();
        //Delete boards before games
        boardRepository.deleteAll();

        //Delete players before game
        playerRepository.deleteAll();

        moveRepository.deleteAll();
        queueRepository.deleteAll();
        firstStackRepository.deleteAll();
        gameRepository.deleteAll();


        //Save game to give it Id
        testGame = new Game();
        testGame.setName("TestGameName");
        testGame = gameService.save(testGame);


        testPlayer = new Player();
        testPlayer.setUserId(12L);
        testPlayer.setUsername("TestUsername");
        testPlayer.setGameId(testGame.getId());
        testPlayer = playerService.save(testPlayer);


        testBoard = boardService.createBoard(testGame.getId());
        testBoard = boardRepository.saveAndFlush(testBoard);

        //Fill the board
        testGame.setCurrentPlayer(testPlayer);
        testGame.addPlayer(testPlayer);
        testGame.setCreatorId(testPlayer.getUserId());
        testGame.setWithBots(false);
        testGame.setBoard(testBoard);
        testGame = gameService.save(testGame);

        testQueue = new PlayerQueue();
        testQueue.setGameId(testGame.getId());
        testQueue.addUserId(testPlayer.getUserId());
        testQueue = queueService.save(testQueue);

        //Create a history for the game
        historyService.createGameHistory(testGame.getId());

        testMove = new PassMove();
        testMove.setUserId(testPlayer.getUserId());
        testMove.setGameId(testGame.getId());
    }

    @AfterEach
    void teardown() {
        resourceWalletRepository.deleteAll();
        tileRepository.deleteAll();
        boardRepository.deleteAll();
        playerRepository.deleteAll();

        moveRepository.deleteAll();
        firstStackRepository.deleteAll();
        queueRepository.deleteAll();
        gameRepository.deleteAll();
    }

    /**
     * Tests if the gameHistory for the correct gameId is fetched from the repository
     */
    @Test
    void testFindGameHistory() {

        //Find the history that is created in setup
        GameHistory history = historyService.findGameHistory(testGame.getId());

        assertEquals(testGame.getId(), history.getGameId(), "The gameId does not match");
    }

    /**
     * Tests the addMoveToHistory method for a default move history by using a pass move
     */
    @Test
    void testAddMoveToHistory() {

        //The move to be added
        Move move = new PassMove();
        move.setGameId(testGame.getId());
        move.setUserId(testPlayer.getUserId());
        MoveHandler handler = move.getMoveHandler();

        historyService.addMoveToHistory(move, handler);
        List<MoveHistory> moveHistories = historyService.findGameHistory(testGame.getId()).getMoves();

        // The move history should contain one element
        assertEquals(1, moveHistories.size(), "There should be one MoveHistory");

        MoveHistory moveHistory = moveHistories.get(0);

        //The moveHistories moveName should be of passMove
        assertEquals(move.getClass().getSimpleName(), moveHistory.getMoveName());

        assertEquals(move.getUserId(), moveHistory.getUserId(), "The userId does not match");
        assertEquals(testPlayer.getUsername(), moveHistory.getUsername(), "The username does not match");
    }

    @Test
    void testDiceMoveHistory() {
        DiceMove diceMove = new DiceMove();
        diceMove.setGameId(testGame.getId());
        diceMove.setUserId(testPlayer.getUserId());

        //Perform the move on the test game so the possible
        //reliance on method call order is taken into account
        moveService.performMove(diceMove);

        List<MoveHistory> moveHistories = historyService.findGameHistory(testGame.getId()).getMoves();

        // The move history should contain one element
        assertEquals(1, moveHistories.size(), "There should be one MoveHistory");

        MoveHistory moveHistory = moveHistories.get(0);

        //The moveHistories moveName should be of diceMove
        assertEquals(diceMove.getClass().getSimpleName(), moveHistory.getMoveName());

        DiceMoveHistory diceMoveHistory = (DiceMoveHistory) moveHistory;

        assertEquals(diceMove.getUserId(), diceMoveHistory.getUserId(), "The userId does not match");
        assertEquals(testPlayer.getUsername(), diceMoveHistory.getUsername(), "The username does not match");

        //Check if the dice roll was saved (not 0 if it was assigned)
        assertNotEquals(0, diceMoveHistory.getRoll(), "The dice roll was not saved correctly");
    }

    @Test
    void testBuildMoveHistory() {
        BuildMove buildMove = new BuildMove();
        buildMove.setGameId(testGame.getId());
        buildMove.setUserId(testPlayer.getUserId());

        //Add necessary funds to testPlayer
        testPlayer.setWallet(new Settlement().getPrice());

        //find random coordinate
        Coordinate coord = testBoard.getTiles().get(0).getCoordinates().get(0);

        //Add building to move
        Settlement settlement = new Settlement();
        settlement.setCoordinate(coord);
        buildMove.setBuilding(settlement);

        //Perform the move on the test game so the possible
        //reliance on method call order is taken into account
        moveService.performMove(buildMove);

        List<MoveHistory> moveHistories = historyService.findGameHistory(testGame.getId()).getMoves();

        // The move history should contain one element
        assertEquals(1, moveHistories.size(), "There should be one MoveHistory");

        MoveHistory moveHistory = moveHistories.get(0);

        //The moveHistories moveName should be of buildMove
        assertEquals(buildMove.getClass().getSimpleName(), moveHistory.getMoveName());

        BuildMoveHistory buildMoveHistory = (BuildMoveHistory) moveHistory;

        assertEquals(BuildingType.SETTLEMENT, buildMoveHistory.getBuildingType(),
                "The building type does not match");
    }
}
