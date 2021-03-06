package ch.uzh.ifi.seal.soprafs20.service.integration;

import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.*;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.moves.*;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.*;
import ch.uzh.ifi.seal.soprafs20.entity.moves.initial.FirstPassMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.initial.FirstRoadMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.initial.FirstSettlementMove;
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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class MoveServiceIntegrationTest {

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

    @Test
    void testFindMoveById_moveFound() {

        testMove = moveRepository.saveAndFlush(testMove);

        Move found = moveService.findMoveById(testMove.getId());

        assertEquals(testMove, found, "The found move should match the testMove!");
    }

    @Test
    void testFindMoveById_noMoveWithId() {

        moveRepository.deleteAll();

        //Repository is assumed empty
        assertNull(moveService.findMoveById(12L));
    }

    @Test
    void testPerformCardMove_VictoryCard() {
        CardMove cardMove = new CardMove();
        setupTestMove(cardMove, testPlayer, testGame);

        // add development card
        DevelopmentCard card = new DevelopmentCard(DevelopmentType.VICTORYPOINT);
        cardMove.setDevelopmentCard(card);
        testPlayer.addDevelopmentCard(card);
        testPlayer = playerService.save(testPlayer);

        // perform
        moveService.performMove(cardMove);

        // assert that the card did not got removed, since it is VP card
        assertEquals(1, testPlayer.getDevelopmentCards().size(),
                "the dev-card should not be deducted");
    }


    //Exemplary for any move, individual perform functions tested separately
    @Test
    void testPerformPassMove() {

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
        moveService.performMove(testMove);
        testGame = gameService.findGame(testGame);
        //The next player should now be the current player
        assertEquals(nextPlayer.getUserId(), testGame.getCurrentPlayer().getUserId());
    }

    @Test
    void testMakeRecalculations() {

        //empty handler that does no move calculations, as they are tested separately
        MoveHandler testHandler = new MoveHandler() {
            @Override
            public void perform(Move move, MoveService moveService) {
            }

            @Override
            public List<Move> calculateNextMoves(Game game, Move move) {
                return new ArrayList<>();
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
    void testMakeRecalculation_playerWins() {
        //empty handler that does no move calculations, as they are tested separately
        MoveHandler testHandler = new MoveHandler() {
            @Override
            public void perform(Move move, MoveService moveService) {
            }

            @Override
            public List<Move> calculateNextMoves(Game game, Move move) {
                return new ArrayList<>();
            }
        };

        //Add a building to the board for the recalculation of points
        City city = new City();
        city.setUserId(testPlayer.getUserId());
        testBoard.addCity(city);

        Settlement settlement = new Settlement();
        settlement.setUserId(testPlayer.getUserId());
        testBoard.addSettlement(settlement);

        //Add victory cards until player wins
        for (int i = 0; i < GameConstants.WIN_POINTS; i++) {
            DevelopmentCard devCard = new DevelopmentCard();
            devCard.setDevelopmentType(DevelopmentType.VICTORYPOINT);
            testPlayer.addDevelopmentCard(devCard);
        }

        moveService.makeRecalculations(testGame, testHandler, testMove);

        //When game is over, then game teardown is called
        assertTrue(gameRepository.findById(testGame.getId()).isEmpty(), "The game should no longer exist");
        assertEquals(0, moveRepository.findAllByGameId(testGame.getId()).size(),
                "There should be no moves for the game!");
    }

    @Test
    void testMakeSetupRecalculations_minReached() {

        //Delete the added testMove
        moveRepository.deleteAll();

        //Fills the game until the minimum player count is reached (testPlayer is already added)
        for (int i = 0; i < GameConstants.DEFAULT_PLAYER_MINIMUM - 1; i++) {
            Player newPlayer = new Player();
            newPlayer.setUserId((long) (10 * i));
            newPlayer.setUsername(String.format("TestPlayerNumber %d", i));
            newPlayer.setGameId(testGame.getId());
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
    void testMakeSetupRecalculations_notEnoughPlayers() {
        //Delete the testMove from setup
        moveRepository.deleteAll();

        //There is only the testPlayer in the game
        moveService.makeSetupRecalculations(testGame);
        //the moves should be empty
        List<Move> moves = moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());
        assertEquals(0, moves.size(), "There should be no moves if player minimum is not reached!");
    }

    @Test
    void testPerformDiceMove() {

        DiceMove diceMove = new DiceMove();
        setupTestMove(diceMove, testPlayer, testGame);
        moveService.performMove(diceMove);

        List<Move> moves = moveRepository.findAllByGameId(testGame.getId());

        assertTrue(moves.size() > 0, "There should be at least one move!");

        //After a dice move there must either be all knight moves or one move must be a passMove
        int onePassMove = 0;

        //Test if there are only nightMoves or one is a pass move and none are knightmoves
        if (moves.get(0).getClass().equals(KnightMove.class)) {
            for (Move move : moves) {
                assertEquals(KnightMove.class, move.getClass());
            }
        }
        else {
            for (Move move : moves) {
                assertNotEquals(KnightMove.class, move.getClass());

                //Test if it is a passmove
                if (move.getClass().equals(PassMove.class)) {
                    onePassMove++;
                }
            }

            assertEquals(1, onePassMove, "There should be exactly one passMove");
        }
    }

    private void setupTestMove(Move move, Player player, Game game) {
        move.setUserId(player.getUserId());
        move.setGameId(game.getId());
    }

    @Test
    void testPerformStartMove() {

        StartMove startMove = new StartMove();
        setupTestMove(startMove, testPlayer, testGame);

        moveService.performMove(startMove);

        //The startMove creates a stack for the game
        List<FirstStack> stacks = firstStackRepository.findAll();
        assertEquals(1, stacks.size(), "There should be one stack for the testGame");
        assertEquals(testGame.getId(), stacks.get(0).getGameId(),
                "The stack does not belong to the right game!");
    }

    @Test
    void testPerformFirstPassMove() {
        Player secondPlayer = setupSecondTestPlayer();

        FirstPassMove firstPassMove = new FirstPassMove();
        setupTestMove(firstPassMove, testPlayer, testGame);

        //Setup a stack for the stackService
        firstStackService.createStackForGameWithId(testGame.getId());

        //Perform
        moveService.performMove(firstPassMove);

        //Refresh game
        testGame = gameService.findGame(testGame);

        assertEquals(secondPlayer.getUserId(), testGame.getCurrentPlayer().getUserId(),
                "The second player should be current player now");
    }

    private Player setupSecondTestPlayer() {
        //Add a player that can be passed to
        Player secondPlayer = new Player();
        secondPlayer.setUsername("secondPlayer");
        secondPlayer.setUserId(22L);
        secondPlayer.setGameId(testGame.getId());
        secondPlayer = playerService.save(secondPlayer);
        testGame.addPlayer(secondPlayer);
        testGame = gameService.save(testGame);
        return secondPlayer;
    }

    /**
     * Test for the execution of a FirstSettlementMove and the resulting game state
     */
    @Test
    void testPerformFirstSettlementMove_availableRoadMoves() {
        FirstSettlementMove firstSettlementMove = new FirstSettlementMove();
        setupTestMove(firstSettlementMove, testPlayer, testGame);

        //find random coordinate
        Coordinate coord = testBoard.getTiles().get(0).getCoordinates().get(0);

        //Add building to move
        Settlement settlement = new Settlement();
        settlement.setCoordinate(coord);
        firstSettlementMove.setBuilding(settlement);

        //Perform
        moveService.performMove(firstSettlementMove);

        //There should now be first road moves available
        List<Move> moves = moveRepository.findAllByGameId(testGame.getId());

        //There should be 2 or 3 moves
        assertThat(moves.size(), anyOf(is(3), is(2)));

        //They should all be road moves
        for (Move move : moves) {
            assertEquals(FirstRoadMove.class, move.getClass(), "All moves should be FirstRoadMoves");
        }

        assertEquals(1, testBoard.getSettlements().size(), "There should be a settlement");
        assertNotNull(testBoard.getSettlements().get(0), "The settlement should not be null");
    }

    @Test
    void testPerformFirstSettlementMove_receiveResources() {

        // setup move
        FirstSettlementMove firstSettlementMove = new FirstSettlementMove();
        setupTestMove(firstSettlementMove, testPlayer, testGame);

        //find random coordinate
        Coordinate coord = testBoard.getTiles().get(0).getCoordinates().get(0);

        //Add building to move
        Settlement settlement = new Settlement();
        settlement.setCoordinate(coord);
        firstSettlementMove.setBuilding(settlement);

        // setup player wallet
        testPlayer.setWallet(new ResourceWallet());

        // setup board
        for (Tile tile : testBoard.getTiles()) {
            if (tile.getCoordinates().contains(coord)) {
                tile.setType(TileType.HILL);
            }
        }

        // perform
        moveService.performMove(firstSettlementMove);

        // assert
        assertFalse(testPlayer.getWallet().isEmpty(),
                "the player should have received resources");
        // the player should receive one resource per adjacent tile = 3 (building at edge only 2 or 1)
        assertThat(testPlayer.getWallet().getResourceAmount(ResourceType.BRICK), anyOf(
                equalTo(1), equalTo(2), equalTo(3)));
    }

    @Test
    void testPerformFirstRoadMove() {
        FirstRoadMove firstRoadMove = new FirstRoadMove();
        setupTestMove(firstRoadMove, testPlayer, testGame);

        //find random coordinates
        Coordinate coord1 = testBoard.getTiles().get(0).getCoordinates().get(0);
        Coordinate coord2 = testBoard.getTiles().get(0).getCoordinates().get(1);

        //Add road to move
        Road road = new Road();
        road.setCoordinate1(coord1);
        road.setCoordinate2(coord2);
        firstRoadMove.setBuilding(road);

        //Perform
        moveService.performMove(firstRoadMove);

        //there should now be a pass move available
        List<Move> moves = moveRepository.findAllByGameId(testGame.getId());

        assertEquals(1, moves.size(), "There should only be one move");
        assertEquals(FirstPassMove.class, moves.get(0).getClass());
        assertEquals(1, testBoard.getRoads().size(), "There should be one road");
        assertNotNull(testBoard.getRoads().get(0), "Road should not be null");
    }

    @Test
    void testPerformBuildMove_buildSettlement() {
        BuildMove buildMove = new BuildMove();
        setupTestMove(buildMove, testPlayer, testGame);

        //Add necessary funds to testPlayer
        testPlayer.setWallet(new Settlement().getPrice());

        //find random coordinate
        Coordinate coord = testBoard.getTiles().get(0).getCoordinates().get(0);

        //Add building to move
        Settlement settlement = new Settlement();
        settlement.setCoordinate(coord);
        buildMove.setBuilding(settlement);

        //Perform
        moveService.performMove(buildMove);

        //refresh Player
        testPlayer = playerService.findPlayerByUserId(testPlayer.getUserId());

        for (ResourceType type : testPlayer.getWallet().getAllTypes()) {
            assertEquals(0, testPlayer.getWallet().getResourceAmount(type),
                    "Wallet should be empty!");
        }


        assertTrue(testBoard.hasBuildingWithCoordinate(coord), "There should be a building");

        //There is only one settlement
        assertEquals(1, testBoard.getSettlements().size());
    }

    @Test
    void testPerformBuildMove_buildCity() {

        //find random coordinate
        Coordinate coord = testBoard.getTiles().get(0).getCoordinates().get(0);

        //Set a settlement to be built on
        Settlement settlement = new Settlement();
        settlement.setCoordinate(coord);
        settlement.setUserId(testPlayer.getUserId());

        //Create the city to be built
        City city = new City();
        city.setCoordinate(coord);
        city.setUserId(testPlayer.getUserId());

        testBoard.addSettlement(settlement);
        testBoard = boardRepository.save(testBoard);

        //Add the funds to the player
        testPlayer.setWallet(new City().getPrice());
        testPlayer = playerService.save(testPlayer);

        //Init the buildMove
        BuildMove buildMove = new BuildMove();
        setupTestMove(buildMove, testPlayer, testGame);
        buildMove.setBuilding(city);

        //Perform
        moveService.performMove(buildMove);

        //Check the players funds
        for (ResourceType type : testPlayer.getWallet().getAllTypes()) {
            assertEquals(0, testPlayer.getWallet().getResourceAmount(type),
                    "Wallet should be empty!");
        }

        assertTrue(testBoard.hasBuildingWithCoordinate(coord), "There should be a building");

        //There should not be a settlement anymore
        assertEquals(0, testBoard.getSettlements().size(),
                "Settlement was not removed correctly");

        assertEquals(1, testBoard.getCities().size(), "There should be a city");
        assertNotNull(testBoard.getCities().get(0), "City should not be null!");
    }

    @Test
    void testPerformBuildMove_buildRoad() {

        //Add funds
        testPlayer.setWallet(new Road().getPrice());
        testPlayer = playerService.save(testPlayer);

        //find random coordinates
        Coordinate coord1 = testBoard.getTiles().get(0).getCoordinates().get(0);
        Coordinate coord2 = testBoard.getTiles().get(0).getCoordinates().get(1);

        //Coordinates to road
        Road road = new Road();
        road.setCoordinate1(coord1);
        road.setCoordinate2(coord2);
        road.setUserId(testPlayer.getUserId());

        BuildMove buildMove = new BuildMove();
        setupTestMove(buildMove, testPlayer, testGame);
        buildMove.setBuilding(road);

        //perform
        moveService.performMove(buildMove);

        //Check the players funds
        for (ResourceType type : testPlayer.getWallet().getAllTypes()) {
            assertEquals(0, testPlayer.getWallet().getResourceAmount(type),
                    "Wallet should be empty!");
        }

        assertTrue(testBoard.hasRoadWithCoordinates(coord2, coord1),
                "Road not built on given coordinates");
        assertEquals(1, testBoard.getRoads().size(), "There must be one road");
        assertNotNull(testBoard.getRoads().get(0), "The road should not be null");
    }

    @Test
    void testPerformCardMove_KnightMove() {
        CardMove cardMove = new CardMove();
        setupTestMove(cardMove, testPlayer, testGame);

        //Add development card
        DevelopmentCard card = new DevelopmentCard(DevelopmentType.KNIGHT);
        cardMove.setDevelopmentCard(card);
        testPlayer.addDevelopmentCard(card);
        testPlayer = playerService.save(testPlayer);

        //Perform
        moveService.performMove(cardMove);

        //Assert that follow up moves are of type
        List<Move> moves = moveRepository.findAllByGameId(testGame.getId());

        //All next moves are KnightMoves
        assertEquals(testBoard.getTiles().size(), moves.size(), "There should be a move per tile");
        for (Move move : moves) {
            assertEquals(KnightMove.class, move.getClass(),
                    "When a Knight Card is played, then KnightMoves will be calculated");
        }

        assertEquals(0, testPlayer.getDevelopmentCards().size(),
                "the dev-card should be deducted");
    }

    @Test
    void testCardMove_RoadProgress_valid() {
        CardMove cardMove = new CardMove();
        setupTestMove(cardMove, testPlayer, testGame);

        //Add development card
        DevelopmentCard card = new DevelopmentCard(DevelopmentType.ROADPROGRESS);
        cardMove.setDevelopmentCard(card);
        testPlayer.addDevelopmentCard(card);
        testPlayer = playerService.save(testPlayer);

        // add settlement to build on
        Settlement settlement = new Settlement();
        settlement.setUserId(testPlayer.getUserId());
        settlement.setCoordinate(testBoard.getAllCoordinates().get(0));
        testBoard.addSettlement(settlement);

        //Perform
        moveService.performMove(cardMove);

        //Assert that follow up moves are of type
        List<Move> moves = moveRepository.findAllByGameId(testGame.getId());

        //All next moves are RoadProgressMoves
        for (Move move : moves) {
            assertEquals(RoadProgressMove.class, move.getClass(),
                    "When a RoadProgress Card is played RoadProgressMoves will be calculated");
        }

        assertEquals(0, testPlayer.getDevelopmentCards().size(),
                "the dev-card should be deducted");
    }

    @Test
    void testCardMove_RoadProgress_BuildOneRoad_valid() {
        CardMove cardMove = new CardMove();
        setupTestMove(cardMove, testPlayer, testGame);

        //Add development card
        DevelopmentCard card = new DevelopmentCard(DevelopmentType.ROADPROGRESS);
        cardMove.setDevelopmentCard(card);
        testPlayer.addDevelopmentCard(card);
        testPlayer = playerService.save(testPlayer);

        // add settlement to build on
        Settlement settlement = new Settlement();
        settlement.setUserId(testPlayer.getUserId());
        settlement.setCoordinate(testBoard.getAllCoordinates().get(0));
        testBoard.addSettlement(settlement);

        // perform card move
        moveService.performMove(cardMove);

        // get followup moves
        List<Move> moves = moveRepository.findAllByGameId(testGame.getId());

        // perform buildMove (build first road)
        moveService.performMove(moves.get(0));

        // get followup moves
        List<Move> followUpMoves = moveRepository.findAllByGameId(testGame.getId());

        // next move is roadProgress move
        for (Move move : followUpMoves) {
            assertEquals(RoadProgressMove.class, move.getClass(),
                    "When a RoadProgress Card is played RoadProgressMoves will be calculated");
        }

        assertEquals(0, testPlayer.getDevelopmentCards().size(),
                "the dev-card should be deducted");
    }

    @Test
    void testCardMove_RoadProgress_noBuildingsToConnect_Invalid() {
        CardMove cardMove = new CardMove();
        setupTestMove(cardMove, testPlayer, testGame);

        //Add development card
        DevelopmentCard card = new DevelopmentCard(DevelopmentType.ROADPROGRESS);
        cardMove.setDevelopmentCard(card);
        testPlayer.addDevelopmentCard(card);
        testPlayer = playerService.save(testPlayer);

        // testPlayer does NOT have any buildings (needed to build a road)

        // perform
        moveService.performMove(cardMove);

        // get moves
        List<Move> moves = moveRepository.findAllByGameId(testGame.getId());

        // assert move type
        for (Move move : moves) {
            assertEquals(PassMove.class, move.getClass(),
                    "Since the player does not have any building options, a passmove should occur");
        }

        assertEquals(0, testPlayer.getDevelopmentCards().size(),
                "the dev-card should be deducted");
    }

    @Test
    void testCardMove_RoadProgress_maxNumberOfRoadsReached_Invalid() {
        CardMove cardMove = new CardMove();
        setupTestMove(cardMove, testPlayer, testGame);

        //Add development card
        DevelopmentCard card = new DevelopmentCard(DevelopmentType.ROADPROGRESS);
        cardMove.setDevelopmentCard(card);
        testPlayer.addDevelopmentCard(card);
        testPlayer = playerService.save(testPlayer);

        // player has already max amount of allowed roads build on board
        for (int i = 0; i < PlayerConstants.MAX_NUMBER_ROADS; i++) {
            Road road = new Road();
            road.setUserId(testPlayer.getUserId());
            testBoard.addRoad(road);
        }

        // perform
        moveService.performMove(cardMove);

        // get moves
        List<Move> moves = moveRepository.findAllByGameId(testGame.getId());

        // assert move type
        for (Move move : moves) {
            assertEquals(PassMove.class, move.getClass(),
                    "since the player reached his max number of roads, a passMove should occur");
        }

        assertEquals(0, testPlayer.getDevelopmentCards().size(),
                "the dev-card should be deducted");
    }

    @Test
    void testCardMove_PlentyProgress() {
        CardMove cardMove = new CardMove();
        setupTestMove(cardMove, testPlayer, testGame);

        //Add development card
        DevelopmentCard card = new DevelopmentCard(DevelopmentType.PLENTYPROGRESS);
        cardMove.setDevelopmentCard(card);
        testPlayer.addDevelopmentCard(card);
        testPlayer = playerService.save(testPlayer);

        //Perform
        moveService.performMove(cardMove);

        //Assert that follow up moves are of type
        List<Move> moves = moveRepository.findAllByGameId(testGame.getId());

        //All next moves are PlentyMoves
        assertEquals(PlentyType.values().length, moves.size(),
                "there should be a separate move for every plenty type");
        for (Move move : moves) {
            assertEquals(PlentyMove.class, move.getClass(),
                    "When a PlentyProgress Card is played, then PlentyMoves will be calculated");
        }

        assertEquals(0, testPlayer.getDevelopmentCards().size(),
                "the dev-card should be deducted");
    }

    @Test
    void testCardMove_MonopolyProgress() {
        CardMove cardMove = new CardMove();
        setupTestMove(cardMove, testPlayer, testGame);

        //Add development card
        DevelopmentCard card = new DevelopmentCard(DevelopmentType.MONOPOLYPROGRESS);
        cardMove.setDevelopmentCard(card);
        testPlayer.addDevelopmentCard(card);
        testPlayer = playerService.save(testPlayer);

        //Perform
        moveService.performMove(cardMove);

        //Assert that follow up moves are of type
        List<Move> moves = moveRepository.findAllByGameId(testGame.getId());

        assertEquals(ResourceType.values().length, moves.size(), "There should be one monopoly move per resource Type");
        //All next moves are MonopolyMoves
        for (Move move : moves) {
            assertEquals(MonopolyMove.class, move.getClass(),
                    "When a Monopoly Card is played MonopolyMoves will be calculated");
        }

        assertEquals(0, testPlayer.getDevelopmentCards().size(),
                "the dev-card should be deducted");
    }

    @Test
    void testPerformTradeMove() {
        TradeMove tradeMove = new TradeMove();
        setupTestMove(tradeMove, testPlayer, testGame);
        tradeMove.setNeededType(ResourceType.WOOL);
        tradeMove.setOfferedType(ResourceType.BRICK);

        //Give player trade funds
        testPlayer.getWallet().addResource(ResourceType.BRICK, GameConstants.TRADE_WITH_BANK_RATIO);
        testPlayer = playerService.save(testPlayer);

        //perform
        moveService.performMove(tradeMove);

        //Assert the player has no wool but one bick
        testPlayer = playerService.findPlayerByUserId(testPlayer.getUserId());
        assertEquals(0, testPlayer.getWallet().getResourceAmount(ResourceType.BRICK),
                "There should be no more bricks");
        assertEquals(1, testPlayer.getWallet().getResourceAmount(ResourceType.WOOL),
                "there should be one wool");
    }

    @Test
    void testPerformPurchaseMove_oneBuy() {
        PurchaseMove purchaseMove = new PurchaseMove();
        setupTestMove(purchaseMove, testPlayer, testGame);

        // give the player purchase funds for one card
        testPlayer.setWallet(new DevelopmentCard().getPrice());
        testPlayer = playerService.save(testPlayer);

        // perform
        moveService.performMove(purchaseMove);

        // assert that the player got one development card and has paid for it
        testPlayer = playerService.findPlayerByUserId(testPlayer.getUserId());
        assertEquals(1, testPlayer.getDevelopmentCards().size(),
                "There should be a development card added");

        for (ResourceType type : testPlayer.getWallet().getAllTypes()) {
            assertEquals(0, testPlayer.getWallet().getResourceAmount(type),
                    "Wallet should be empty!");
        }

        List<Move> moves = moveRepository.findAllByGameId(testGame.getId());
        assertEquals(1, moves.size(), "only one move should follow");
        assertEquals(PassMove.class, moves.get(0).getClass(), "a pass move must follow," +
                "as the player only can afford one development card");
    }

    @Test
    void testPerformPurchaseMove_twoBuys() {
        PurchaseMove purchaseMove = new PurchaseMove();
        setupTestMove(purchaseMove, testPlayer, testGame);

        // give the player purchase funds for two cards
        ResourceWallet funds = new ResourceWallet();
        ResourceWallet price = new DevelopmentCard().getPrice();

        for (ResourceType type: price.getAllTypes()) {
            funds.addResource(type, 2 * price.getResourceAmount(type));
        }

        testPlayer.setWallet(funds);
        testPlayer = playerService.save(testPlayer);

        // perform
        moveService.performMove(purchaseMove);

        // assert that the player got one development card and has paid for it
        testPlayer = playerService.findPlayerByUserId(testPlayer.getUserId());
        assertEquals(1, testPlayer.getDevelopmentCards().size(),
                "There should be a development card added");
        for (ResourceType type : testPlayer.getWallet().getAllTypes()) {
            assertEquals(price.getResourceAmount(type), testPlayer.getWallet().getResourceAmount(type),
                    "the player must have paid for the card and only can afford one more card");
        }

        List<Move> moves = moveRepository.findAllByGameId(testGame.getId());

        assertEquals(2, moves.size(), "another purchase move or a pass move must follow");
        for (Move move : moves) {
            assertThat(move.getClass(), anyOf(equalTo(PurchaseMove.class), equalTo(PassMove.class)));
        }
    }

    @Test
    void testPerformMonopolyMove() {
        MonopolyMove monopolyMove = new MonopolyMove();
        setupTestMove(monopolyMove, testPlayer, testGame);
        monopolyMove.setMonopolyType(ResourceType.ORE);

        testPlayer.setWallet(new ResourceWallet());

        // support opponents with funds
        Player opponent = setupSecondTestPlayer();

        ResourceWallet funds1 = new ResourceWallet();
        opponent.setWallet(funds1);

        for (ResourceType type : ResourceType.values()) {
            funds1.addResource(type, 3);
        }

        testPlayer = playerService.save(testPlayer);
        opponent = playerService.save(opponent);

        // perform
        moveService.performMove(monopolyMove);

        // assert that the test player monopolized the
        testPlayer = playerService.findPlayerByUserId(testPlayer.getUserId());
        opponent = playerService.findPlayerByUserId(opponent.getUserId());

        assertEquals(0, opponent.getWallet().getResourceAmount(ResourceType.ORE),
                "the monopolized resource should have been removed");
        assertEquals(3, opponent.getWallet().getResourceAmount(ResourceType.BRICK),
                "this resource should not have been removed");
        assertEquals(3, testPlayer.getWallet().getResourceAmount(ResourceType.ORE),
                "the monopolized resource should have been added to the player");
    }

    @Test
    void testPerformPlentyMove_Miner() {
        PlentyMove plentyMove = new PlentyMove();
        setupTestMove(plentyMove, testPlayer, testGame);
        plentyMove.setPlentyType(PlentyType.MINER);

        testPlayer.setWallet(new ResourceWallet());
        testPlayer = playerService.save(testPlayer);

        // perform
        moveService.performMove(plentyMove);

        // assert that the plenty resources are added to the players' wallet
        testPlayer = playerService.findPlayerByUserId(testPlayer.getUserId());

        assertEquals(2, testPlayer.getWallet().getResourceAmount(ResourceType.BRICK),
                "this plenty resource should have been added");
        assertEquals(2, testPlayer.getWallet().getResourceAmount(ResourceType.ORE),
                "this plenty resource should have been added");
    }

    @Test
    void testPerformPlentyMove_Farmer() {
        PlentyMove plentyMove = new PlentyMove();
        setupTestMove(plentyMove, testPlayer, testGame);
        plentyMove.setPlentyType(PlentyType.FARMER);

        testPlayer.setWallet(new ResourceWallet());
        testPlayer = playerService.save(testPlayer);

        // perform
        moveService.performMove(plentyMove);

        // assert that the plenty resources are added to the players' wallet
        testPlayer = playerService.findPlayerByUserId(testPlayer.getUserId());

        assertEquals(2, testPlayer.getWallet().getResourceAmount(ResourceType.WOOL),
                "this plenty resource should have been added");
        assertEquals(2, testPlayer.getWallet().getResourceAmount(ResourceType.GRAIN),
                "this plenty resource should have been added");
    }

    @Test
    void testPerformPlentyMove_Lumberjack() {
        PlentyMove plentyMove = new PlentyMove();
        setupTestMove(plentyMove, testPlayer, testGame);
        plentyMove.setPlentyType(PlentyType.LUMBERJACK);

        testPlayer.setWallet(new ResourceWallet());
        testPlayer = playerService.save(testPlayer);

        // perform
        moveService.performMove(plentyMove);

        // assert that the plenty resources are added to the players' wallet
        testPlayer = playerService.findPlayerByUserId(testPlayer.getUserId());

        assertEquals(5, testPlayer.getWallet().getResourceAmount(ResourceType.LUMBER),
                "this plenty resource should have been added");
    }

    @Test
    void testPerformRoadProgressMove() {
        RoadProgressMove roadProgressMove = new RoadProgressMove();
        setupTestMove(roadProgressMove, testPlayer, testGame);

        //find random coordinates
        Coordinate coord1 = testBoard.getTiles().get(0).getCoordinates().get(0);
        Coordinate coord2 = testBoard.getTiles().get(0).getCoordinates().get(1);

        //Add road to move
        Road road = new Road();
        road.setCoordinate1(coord1);
        road.setCoordinate2(coord2);
        roadProgressMove.setBuilding(road);

        //Perform
        moveService.performMove(roadProgressMove);

        // assert
        assertTrue(testBoard.hasRoadWithCoordinates(coord2, coord1),
                "Road not built on given coordinates");
        assertEquals(1, testBoard.getRoads().size(), "There should be one road");
        assertNotNull(testBoard.getRoads().get(0), "Road should not be null");

    }

    @Test
    void testPerformKnightMove() {
        KnightMove knightMove = new KnightMove();
        setupTestMove(knightMove, testPlayer, testGame);
        knightMove.setTileId(testBoard.getTiles().get(0).getId());

        testBoard.getTiles().get(1).setRobber(true);

        // place settlement of victim on tile with newly assigned robber
        Player victim = setupSecondTestPlayer();
        Coordinate coordinate = testBoard.getTiles().get(0).getCoordinates().get(0);
        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate);
        settlement.setUserId(victim.getUserId());
        testBoard.addSettlement(settlement);

        // perform
        moveService.performMove(knightMove);

        assertTrue(testBoard.getTiles().get(0).isRobber(),
                "robber must be placed on this tile");
        assertFalse(testBoard.getTiles().get(1).isRobber(),
                "robber must be removed from tile");

        List<Move> moves = moveRepository.findAllByGameId(testGame.getId());
        for (Move move : moves) {
            assertEquals(StealMove.class, move.getClass(),
                    "after a knight move gets performed, a steal move must follow");
        }
    }

    @Test
    void testPerformStealMove() {
        StealMove stealMove = new StealMove();
        setupTestMove(stealMove, testPlayer, testGame);
        Player victim = setupSecondTestPlayer();
        stealMove.setVictimId(victim.getUserId());

        testPlayer.setWallet(new ResourceWallet());

        // supply victim with resources
        ResourceWallet funds = new ResourceWallet();
        funds.addResource(ResourceType.ORE, 3);

        victim.setWallet(funds);

        testPlayer = playerService.save(testPlayer);
        victim = playerService.save(victim);

        // perform
        moveService.performMove(stealMove);

        assertEquals(1, testPlayer.getWallet().getResourceAmount(ResourceType.ORE),
                "the stolen resource must be added");
        assertEquals(2, victim.getWallet().getResourceAmount(ResourceType.ORE),
                "1 resource must be stolen from victim");

    }

    @Test
    void testPerformStealMove_victimHasEmptyWallet() {
        StealMove stealMove = new StealMove();
        setupTestMove(stealMove, testPlayer, testGame);
        Player victim = setupSecondTestPlayer();
        stealMove.setVictimId(victim.getUserId());

        // set wallet for player
        ResourceWallet funds = new ResourceWallet();
        funds.addResource(ResourceType.ORE, 1);
        testPlayer.setWallet(funds);

        // assume victim has no resources
        victim.setWallet(new ResourceWallet());

        testPlayer = playerService.save(testPlayer);
        victim = playerService.save(victim);

        // perform
        moveService.performMove(stealMove);

        assertEquals(1, testPlayer.getWallet().getResourceAmount(ResourceType.ORE),
                "no resource should be added");
        assertEquals(0, victim.getWallet().getResourceAmount(ResourceType.ORE),
                "the victim does not have any resources");

    }

}
