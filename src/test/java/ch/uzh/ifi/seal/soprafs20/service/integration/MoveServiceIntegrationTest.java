package ch.uzh.ifi.seal.soprafs20.service.integration;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.GameConstants;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.PlayerQueue;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.moves.DiceMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.PassMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.StartMove;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.QueueService;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@WebAppConfiguration
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
public class MoveServiceIntegrationTest {

    @Qualifier("tileRepository")
    @Autowired
    TileRepository tileRepository;

    @Autowired
    BoardService boardService;

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
    public void setup() {

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
        gameRepository.deleteAll();


        testPlayer = new Player();
        testPlayer.setUserId(12L);
        testPlayer.setUsername("TestUsername");
        testPlayer = playerService.save(testPlayer);

        testBoard = boardService.createBoard();
        testBoard = boardRepository.saveAndFlush(testBoard);

        testGame = new Game();
        testGame.setName("TestGameName");
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


        testMove = new PassMove();
        testMove.setUserId(testPlayer.getUserId());
        testMove.setGameId(testGame.getId());
        testMove = moveRepository.saveAndFlush(testMove);
    }

    @AfterEach
    public void teardown() {
        resourceWalletRepository.deleteAll();
        tileRepository.deleteAll();
        boardRepository.deleteAll();
        playerRepository.deleteAll();

        moveRepository.deleteAll();
        queueRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    public void testFindMoveById_moveFound() {

        Move found = moveService.findMoveById(testMove.getId());

        assertEquals(testMove, found, "The found move should match the testMove!");
    }

    @Test
    public void testFindMoveById_noMoveWithId() {

        moveRepository.deleteAll();

        //Repository is assumed empty
        assertNull(moveService.findMoveById(12L));
    }


    //Exemplary for any move, individual perform functions tested separately
    @Test
    public void testPerformMove_performAPassMove() {

        //Setup
        Player nextPlayer = new Player();
        nextPlayer.setUsername("NextPlayer");
        nextPlayer.setUserId(22L);
        testGame.addPlayer(nextPlayer);
        testGame = gameService.save(testGame);
        testQueue.addUserId(nextPlayer.getUserId());
        testQueue = queueService.save(testQueue);

        //then
        moveService.performMove(testMove);
        testGame = gameService.findGame(testGame);
        //The next player should now be the current player
        assertEquals(nextPlayer.getUserId(), testGame.getCurrentPlayer().getUserId());
    }

    @Test
    public void testMakeRecalculations() {

        //empty handler that does no move calculations, as they are tested separately
        MoveHandler testHandler = new MoveHandler() {
            @Override
            public List<Move> calculateNextMoves(Game game, Move move) {
                return new ArrayList<>();
            }

            @Override
            public void perform(Move move, MoveService moveService) {
            }
        };

        //Add a building to the board for the recalculation of points
        City city = new City();
        city.setUserId(testPlayer.getUserId());
        testBoard.addCity(city);

        Settlement settlement = new Settlement();
        settlement.setUserId(testPlayer.getUserId());
        testBoard.addSettlement(settlement);

        int buildingPoints = (new Settlement().getVictoryPoints() + new City().getVictoryPoints());

        //Add point card to player
        DevelopmentCard devCard = new DevelopmentCard();
        devCard.setDevelopmentType(DevelopmentType.VICTORYPOINT);
        testPlayer.addDevelopmentCard(devCard);
        int cardPoints = 1;

        moveService.makeRecalculations(testGame, testHandler, testMove);

        testPlayer = playerService.findPlayerByUserId(testPlayer.getUserId());
        assertEquals(cardPoints + buildingPoints, testPlayer.getVictoryPoints());

    }

    @Test
    public void testMakeSetupRecalculations_minReached() {

        //Delete the added testMove
        moveRepository.deleteAll();

        //Fills the game until the minimum player count is reached (testPlayer is already added)
        for (int i = 0; i < GameConstants.DEFAULT_PLAYER_MINIMUM - 1; i++) {
            Player newPlayer = new Player();
            newPlayer.setUserId((long) (10 * i));
            newPlayer.setUsername(String.format("TestPlayerNumber %d", i));
            testGame.addPlayer(newPlayer);
            testQueue.addUserId(newPlayer.getUserId());
            testQueue = queueService.save(testQueue);
            testGame = gameService.save(testGame);
            playerService.save(newPlayer);
        }

        moveService.makeSetupRecalculations(testGame);

        //After this there should be a start move in the moveRepo for the testPlayer (set as the current player)
        List<Move> moves = moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());
        assertEquals(1, moves.size(), "There should be a move for the game");
        assertEquals(StartMove.class, moves.get(0).getClass(), "The Move should be of type startMove");
    }

    @Test
    public void testMakeSetupRecalculations_notEnoughPlayers() {
        //Delete the testMove from setup
        moveRepository.deleteAll();

        //There is only the testPlayer in the game
        moveService.makeSetupRecalculations(testGame);
        //the moves should be empty
        List<Move> moves = moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());
        assertEquals(0, moves.size(), "There should be no moves if player minimum is not reached!");
    }

    @Test
    public void testPerformDiceMove() {

        Move diceMove = new DiceMove();
        setupTestMove(diceMove, testPlayer, testGame);
        diceMove.getMoveHandler().perform(diceMove, moveService);

        //TODO: Maybe there is a good assertion strategy
    }

    @Test
    public void testPerformStartMove() {

    }

    @Test
    public void testPerformFirstPassMove() {

    }

    @Test
    public void testPerformFirstSettlementMove() {

    }

    @Test
    public void testPerformFirstRoadMove() {

    }

    @Test
    public void testPerformPassMove() {

    }

    @Test
    public void testPerformBuildMove() {

    }

    @Test
    public void testPerformCardMove() {

    }

    @Test
    public void testPerformTradeMove() {

    }

    @Test
    public void testPerformPurchaseMove() {

    }

    @Test
    public void testPerformMonopolyMove() {

    }

    @Test
    public void testPerformPlentyMove() {

    }

    @Test
    public void testPerformRoadProgressMove() {

    }

    @Test
    public void testPerformKnightMove() {

    }

    @Test
    public void testPerformStealMove() {

    }

    private void setupTestMove(Move move, Player player, Game game) {
        move.setUserId(player.getUserId());
        move.setGameId(game.getId());
    }

}
