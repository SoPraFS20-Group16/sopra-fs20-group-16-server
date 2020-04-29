package ch.uzh.ifi.seal.soprafs20.service.integration;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.GameConstants;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
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
import ch.uzh.ifi.seal.soprafs20.service.FirstStackService;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
public class MoveServiceIntegrationTest {

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
        firstStackRepository.deleteAll();
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
    }

    @AfterEach
    public void teardown() {
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
    public void testFindMoveById_moveFound() {

        testMove = moveRepository.saveAndFlush(testMove);

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
    public void testPerformPassMove() {

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

        DiceMove diceMove = new DiceMove();
        setupTestMove(diceMove, testPlayer, testGame);
        moveService.performMove(diceMove);

        //TODO: Maybe there is a good assertion strategy
    }

    @Test
    public void testPerformStartMove() {

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
    public void testPerformFirstPassMove() {
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

    @Test
    public void testPerformFirstSettlementMove() {
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
    public void testPerformFirstRoadMove() {
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
    public void testPerformBuildMove_buildSettlement() {
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
    public void testPerformBuildMove_buildCity() {

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
    public void testPerformBuildMove_buildRoad() {

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
    public void testPerformCardMove_KnightMove() {
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
    }

    @Test
    public void testCardMove_RoadProgress() {
        CardMove cardMove = new CardMove();
        setupTestMove(cardMove, testPlayer, testGame);

        //Add development card
        DevelopmentCard card = new DevelopmentCard(DevelopmentType.ROADPROGRESS);
        cardMove.setDevelopmentCard(card);
        testPlayer.addDevelopmentCard(card);
        testPlayer = playerService.save(testPlayer);

        //Perform
        moveService.performMove(cardMove);

        //Assert that follow up moves are of type
        List<Move> moves = moveRepository.findAllByGameId(testGame.getId());

        //All next moves are RoadProgressMoves
        for (Move move : moves) {
            assertEquals(RoadProgressMove.class, move.getClass(),
                    "When a RoadProgress Card is played RoadProgressMoves will be calculated");
        }
    }

    @Test
    public void testCardMove_PlentyProgress() {
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
        assertEquals(Math.pow(ResourceType.values().length, 2), moves.size(),
                "There should be a PlentyMove for every combination of two ResourceCards");
        for (Move move : moves) {
            assertEquals(PlentyMove.class, move.getClass(),
                    "When a PlentyProgress Card is played, then PlentyMoves will be calculated");
        }
    }

    @Test
    public void testCardMove_MonopolyProgress() {
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
    }

    @Test
    public void testPerformTradeMove() {
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
    public void testPerformPurchaseMove() {
        PurchaseMove purchaseMove = new PurchaseMove();
        setupTestMove(purchaseMove, testPlayer, testGame);

        // give the player purchase funds
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

    }

    @Test
    public void testPerformMonopolyMove() {
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
    public void testPerformPlentyMove() {
        PlentyMove plentyMove = new PlentyMove();
        setupTestMove(plentyMove, testPlayer, testGame);
        plentyMove.setPlentyType1(ResourceType.BRICK);
        plentyMove.setPlentyType2(ResourceType.LUMBER);

        testPlayer.setWallet(new ResourceWallet());
        testPlayer = playerService.save(testPlayer);

        // perform
        moveService.performMove(plentyMove);

        // assert that the plenty resources are added to the players' wallet
        testPlayer = playerService.findPlayerByUserId(testPlayer.getUserId());

        assertEquals(1, testPlayer.getWallet().getResourceAmount(ResourceType.BRICK),
                "this plenty resource should have been added");
        assertEquals(1, testPlayer.getWallet().getResourceAmount(ResourceType.LUMBER),
                "this plenty resource should have been added");
    }

    @Test
    public void testPerformRoadProgressMove() {
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
    public void testPerformKnightMove() {
        KnightMove knightMove = new KnightMove();
        setupTestMove(knightMove, testPlayer, testGame);
        knightMove.setTileId(testBoard.getTiles().get(0).getId());

        testBoard.getTiles().get(1).setHasRobber(true);

        // perform
        moveService.performMove(knightMove);

        assertTrue(testBoard.getTiles().get(0).hasRobber(),
                "robber must be placed on this tile");
        assertFalse(testBoard.getTiles().get(1).hasRobber(),
                "robber must be removed from tile");

        List<Move> moves = moveRepository.findAllByGameId(testGame.getId());
        for (Move move : moves) {
            assertEquals(StealMove.class, move.getClass(),
                    "after a knight move gets performed, a steal move must follow");
        }
    }

    @Test
    public void testPerformStealMove() {
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

    private void setupTestMove(Move move, Player player, Game game) {
        move.setUserId(player.getUserId());
        move.setGameId(game.getId());
    }

    private Player setupSecondTestPlayer() {
        //Add a player that can be passed to
        Player secondPlayer = new Player();
        secondPlayer.setUsername("secondPlayer");
        secondPlayer.setUserId(22L);
        secondPlayer = playerService.save(secondPlayer);
        testGame.addPlayer(secondPlayer);
        testGame = gameService.save(testGame);
        return secondPlayer;
    }

}
