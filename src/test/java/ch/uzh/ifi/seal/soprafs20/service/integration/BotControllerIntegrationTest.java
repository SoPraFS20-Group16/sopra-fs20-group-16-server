package ch.uzh.ifi.seal.soprafs20.service.integration;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.PlayerQueue;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.PassMove;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import ch.uzh.ifi.seal.soprafs20.service.*;
import ch.uzh.ifi.seal.soprafs20.service.board.BoardService;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebAppConfiguration
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class BotControllerIntegrationTest {

    @Autowired
    BotService botService;

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
        moveRepository.saveAndFlush(testMove);
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
     * Tests the createBot method by creating a bot for the testGame
     */
    @Test
    void testCreateBot() {

        //Adds a second player beside the testPlayer
        Player created = botService.createBot(testGame.getId());

        assertEquals(2, testGame.getPlayers().size(), "There should be two players");
        assertTrue(testGame.isPlayer(created), "The testGame has the bot as a player");
    }

    /**
     * Tests performBotMove, given there is a pass move available for the bot
     */
    @Test
    void testPerformBotMove() {

        //set the testPlayer as a bot
        testPlayer.setBot(true);
        playerService.save(testPlayer);

        //Setup
        Player nextPlayer = new Player();
        nextPlayer.setUsername("NextPlayer");
        nextPlayer.setUserId(22L);
        nextPlayer.setGameId(testGame.getId());
        testGame.addPlayer(nextPlayer);
        testGame = gameService.save(testGame);
        testQueue.addUserId(nextPlayer.getUserId());
        testQueue = queueService.save(testQueue);

        //then
        botService.performBotMove(testGame.getId(), testPlayer.getUserId());
        testGame = gameService.findGame(testGame);


        //The nextPlayer should now be the current player
        assertEquals(nextPlayer.getUserId(), testGame.getCurrentPlayer().getUserId());

    }
}
