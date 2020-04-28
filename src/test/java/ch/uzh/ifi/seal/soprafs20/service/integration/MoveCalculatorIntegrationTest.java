package ch.uzh.ifi.seal.soprafs20.service.integration;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.GameConstants;
import ch.uzh.ifi.seal.soprafs20.constant.PlayerConstants;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.PlayerQueue;
import ch.uzh.ifi.seal.soprafs20.entity.game.ResourceWallet;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.moves.*;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.KnightMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.MonopolyMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.PlentyMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.StealMove;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import ch.uzh.ifi.seal.soprafs20.service.FirstStackService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.QueueService;
import ch.uzh.ifi.seal.soprafs20.service.board.BoardService;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.calculator.MoveCalculator;
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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
public class MoveCalculatorIntegrationTest {


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

    // -- start move(s) --

    @Test
    public void testCalculateStartMove() {

    }

    // -- initial moves --

    @Test
    public void testCalculateFirstPassMove() {

    }

    @Test
    public void testCalculateFirstSettlementMoves() {

    }

    @Test
    public void testCalculateFirstRoadMoves() {

    }

    // -- standard moves --

    // - default moves -

    @Test
    public void testCalculatePassMove() {

    }

    @Test
    public void testCalculateDiceMove() {

    }

    @Test
    public void testCalculateAllStandardMoves() {

    }

    // - build moves -

    @Test
    public void testCalculateRoadMoves_connectingToSettlement_valid() {

        // player can afford road
        testPlayer.setWallet(new Road().getPrice());
        playerService.save(testPlayer);

        // find random coordinate
        Coordinate coordinate = testBoard.getTiles().get(0).getCoordinates().get(0);

        // add settlement on board
        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate);
        settlement.setUserId(testPlayer.getUserId());
        testBoard.addSettlement(settlement);

        // perform
        List<BuildMove> moves = MoveCalculator.calculateRoadMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        // when settlement is build at the rim, only two possible roads can be build
        assertThat(moves.size(), anyOf(equalTo(2), equalTo(3)));
        assertEquals(Road.class, moves.get(1).getBuilding().getClass(),
                "the building should be a road");
        assertTrue(moves.get(0).getBuilding().getCoordinates().contains(coordinate),
                "the building coordinate should be the one of the settlement");
        for (BuildMove move : moves) {
            assertTrue(move.getBuilding().getCoordinates().contains(coordinate),
                    "every possible road should share a coordinate with the connecting settlement");
        }
    }

    @Test
    public void testCalculateRoadMoves_connectingToCity_valid() {
        // player can afford road
        testPlayer.setWallet(new Road().getPrice());
        playerService.save(testPlayer);

        // find random coordinate
        Coordinate coordinate = testBoard.getTiles().get(0).getCoordinates().get(0);

        // add settlement on board
        City city = new City();
        city.setCoordinate(coordinate);
        city.setUserId(testPlayer.getUserId());
        testBoard.addCity(city);

        // perform
        List<BuildMove> moves = MoveCalculator.calculateRoadMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        // when city is build at the rim, only two possible roads can be build
        assertThat(moves.size(), anyOf(equalTo(2), equalTo(3)));
        assertEquals(Road.class, moves.get(1).getBuilding().getClass(),
                "the building should be a road");
        assertTrue(moves.get(0).getBuilding().getCoordinates().contains(coordinate),
                "the building coordinate should be the one of the city");
        for (BuildMove move : moves) {
            assertTrue(move.getBuilding().getCoordinates().contains(coordinate),
                    "every possible road should share a coordinate with the connecting city");
        }
    }

    // TODO: debug calculate getRoadEndPoints (nullPointer)
    /*
    @Test
    public void testCalculateRoadMoves_connectingToRoad_valid() {

        // player can afford road
        testPlayer.setWallet(new Road().getPrice());
        playerService.save(testPlayer);

        // find random coordinate
        Coordinate coordinate = testBoard.getTiles().get(0).getCoordinates().get(0);

        // add settlement on board
        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate);
        settlement.setUserId(testPlayer.getUserId());
        testBoard.addSettlement(settlement);

        // add road on board
        Road road = new Road();
        road.setCoordinate2(coordinate);
        road.setCoordinate2(coordinate.getNeighbors().get(1));
        road.setUserId(testPlayer.getUserId());
        testBoard.addRoad(road);

        // perform
        List<BuildMove> moves = MoveCalculator.calculateRoadMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        // when settlement is build at the rim, only two possible roads can be build
        assertThat(moves.size(), anyOf(equalTo(2), equalTo(4)));
        assertEquals(Road.class, moves.get(1).getBuilding().getClass(),
                "the building should be a road");
        // every possible new road must either contain the coordinate of the settlement or of the new road
        for (BuildMove move: moves) {
            assertThat(move.getBuilding().getCoordinates().get(0), anyOf(equalTo(settlement.getCoordinate()),
                    equalTo(road.getCoordinate1()), equalTo(road.getCoordinate2())));
            assertTrue(move.getBuilding().getCoordinates().contains(coordinate),
                    "every possible road should share a coordinate with the connecting settlement");
        }

    }
    */

    @Test
    public void testCalculateRoadMoves_notEnoughResources() {

        // player has empty resources
        testPlayer.setWallet(new ResourceWallet());
        playerService.save(testPlayer);

        // find random coordinate
        Coordinate coordinate = testBoard.getTiles().get(0).getCoordinates().get(0);

        // add settlement on board
        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate);
        settlement.setUserId(testPlayer.getUserId());
        testBoard.addSettlement(settlement);

        // perform
        List<BuildMove> moves = MoveCalculator.calculateRoadMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(0, moves.size(),
                "the player cannot afford a road move");
    }

    @Test
    public void testCalculateRoadMoves_maxNumberOfRoadsReached() {

        // player has empty resources
        testPlayer.setWallet(new Road().getPrice());
        playerService.save(testPlayer);

        // find random coordinate
        Coordinate coordinate = testBoard.getTiles().get(0).getCoordinates().get(0);

        // add settlement on board
        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate);
        settlement.setUserId(testPlayer.getUserId());
        testBoard.addSettlement(settlement);

        // player has already max amount of allowed roads build on board
        for (int i = 0; i < PlayerConstants.MAX_NUMBER_ROADS; i++) {
            Road road = new Road();
            road.setUserId(testPlayer.getUserId());
            testBoard.addRoad(road);
        }

        // perform
        List<BuildMove> moves = MoveCalculator.calculateRoadMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(0, moves.size(),
                "the player cannot build another road, since max amount is reached");

    }

    @Test
    public void testCalculateRoadMoves_noValidBuildingsToConnect() {

        // player can afford city
        testPlayer.setWallet(new Road().getPrice());
        testPlayer = playerService.save(testPlayer);

        // perform
        List<BuildMove> moves = MoveCalculator.calculateRoadMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(0, moves.size(),
                "a road move cannot be added, since the player does not have any buildings to connect");
    }

    @Test
    public void testCalculateSettlementMoves_valid() {

    }

    @Test
    public void testCalculateSettlementMoves_notEnoughResources() {

    }

    @Test
    public void testCalculateSettlementMoves_maxNumberOfSettlementsReached() {

    }

    @Test
    public void testCalculateSettlementMoves_noValidRoadEndpoints() {

    }

    @Test
    public void testCalculateCityMoves_valid() {

        // player can afford city
        testPlayer.setWallet(new City().getPrice());
        testPlayer = playerService.save(testPlayer);

        // find random coordinate
        Coordinate coordinate = testBoard.getTiles().get(0).getCoordinates().get(0);

        // add settlement on board
        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate);
        settlement.setUserId(testPlayer.getUserId());
        testBoard.addSettlement(settlement);

        // perform
        List<BuildMove> moves = MoveCalculator.calculateCityMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(1, moves.size(),
                "a city move should be added");
        assertEquals(City.class, moves.get(0).getBuilding().getClass(),
                "the building should be a city");
        assertTrue(moves.get(0).getBuilding().getCoordinates().contains(coordinate),
                "the building coordinate should be the one of the settlement");
    }

    @Test
    public void testCalculateCityMoves_notEnoughResources() {

        // empty resource wallet
        testPlayer.setWallet(new ResourceWallet());
        testPlayer = playerService.save(testPlayer);

        // find random coordinate
        Coordinate coordinate = testBoard.getTiles().get(0).getCoordinates().get(0);

        // add settlement on board
        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate);
        settlement.setUserId(testPlayer.getUserId());
        testBoard.addSettlement(settlement);

        // perform
        List<BuildMove> moves = MoveCalculator.calculateCityMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(0, moves.size(),
                "the player cannot afford this move");

    }

    @Test
    public void testCalculateCityMoves_maxNumberOfCitiesReached() {

        // player can afford city
        testPlayer.setWallet(new City().getPrice());
        testPlayer = playerService.save(testPlayer);

        // find random coordinate
        Coordinate coordinate = testBoard.getTiles().get(0).getCoordinates().get(0);

        // add settlement on board
        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate);
        settlement.setUserId(testPlayer.getUserId());
        testBoard.addSettlement(settlement);

        // player has already max amount of allowed cities build on board
        for (int i = 0; i < PlayerConstants.MAX_NUMBER_CITIES; i++) {
            City city = new City();
            city.setUserId(testPlayer.getUserId());
            testBoard.addCity(city);
        }

        // perform
        List<BuildMove> moves = MoveCalculator.calculateCityMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(0, moves.size(),
                "the player cannot build another city, since max amount is reached");

    }

    @Test
    public void testCalculateCityMoves_noSettlementsToBuildOn() {

        // player can afford city
        testPlayer.setWallet(new City().getPrice());
        testPlayer = playerService.save(testPlayer);

        // perform
        List<BuildMove> moves = MoveCalculator.calculateCityMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(0, moves.size(),
                "a city move cannot be added, since the player does not have any settlements");
    }

    // - card moves -

    @Test
    public void testCalculateCardMoves_valid() {

        // player has development card
        DevelopmentCard developmentCard = new DevelopmentCard();
        developmentCard.setDevelopmentType(DevelopmentType.MONOPOLYPROGRESS);
        testPlayer.addDevelopmentCard(developmentCard);
        testPlayer = playerService.save(testPlayer);

        // perform
        List<CardMove> moves = MoveCalculator.calculateCardMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(1, moves.size(),
                "a cardMove must be added to to player");
        assertEquals(CardMove.class, moves.get(0).getClass(),
                "the move must be a cardMove");
    }

    @Test
    public void testCalculateCardMoves_VictoryPointCard_resultsInNoMove() {

        // player has development card
        DevelopmentCard developmentCard = new DevelopmentCard();
        developmentCard.setDevelopmentType(DevelopmentType.VICTORYPOINT);
        testPlayer.addDevelopmentCard(developmentCard);
        testPlayer = playerService.save(testPlayer);

        // perform
        List<CardMove> moves = MoveCalculator.calculateCardMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(0, moves.size(),
                "a victory point card does not produce a cardMove");
    }

    @Test
    public void testCalculateCardMoves_noCard() {

        // player has no development card
        testPlayer.setDevelopmentCards(new ArrayList<>());
        testPlayer = playerService.save(testPlayer);

        // perform
        List<CardMove> moves = MoveCalculator.calculateCardMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(0, moves.size(),
                "without a development card, no card move gets created");
    }

    // - other moves -

    @Test
    public void testCalculateTradeMoves_oneTrade_valid() {

        // player has enough funds for one trade
        ResourceWallet resourceWallet = new ResourceWallet();
        for (int i = 0; i < GameConstants.TRADE_WITH_BANK_RATIO; i++) {
            resourceWallet.addResource(ResourceType.GRAIN, 1);
        }
        testPlayer.setWallet(resourceWallet);
        testPlayer = playerService.save(testPlayer);

        // perform
        List<Move> moves = MoveCalculator.calculateTradeMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(ResourceType.values().length - 1, moves.size(),
                "a trade move must be added for every resource type (minus offered resource type)");
        assertEquals(TradeMove.class, moves.get(0).getClass(),
                "the added move must be a trade move");
        for (Move move : moves) {
            TradeMove tradeMove = (TradeMove) move;
            assertNotEquals(ResourceType.GRAIN, tradeMove.getNeededType(),
                    "offered type cannot be needed type");
        }
    }

    @Test
    public void testCalculateTradeMoves_twoTrades_valid() {

        // player has enough funds for one trade
        ResourceWallet resourceWallet = new ResourceWallet();
        for (int i = 0; i < GameConstants.TRADE_WITH_BANK_RATIO; i++) {
            resourceWallet.addResource(ResourceType.GRAIN, 1);
        }
        for (int i = 0; i < GameConstants.TRADE_WITH_BANK_RATIO; i++) {
            resourceWallet.addResource(ResourceType.WOOL, 1);
        }
        testPlayer.setWallet(resourceWallet);
        testPlayer = playerService.save(testPlayer);

        // perform
        List<Move> moves = MoveCalculator.calculateTradeMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(ResourceType.values().length * 2 - 2, moves.size(),
                "a trade moves must be added for every resource type twice, minus offered two types");
        for (Move move : moves) {
            assertEquals(TradeMove.class, move.getClass(),
                    "the added move must be a trade move");
        }
    }

    @Test
    public void testCalculateTradeMoves_notEnoughResources() {

        // player has NOT enough funds for one trade
        ResourceWallet resourceWallet = new ResourceWallet();
        for (int i = 0; i < (GameConstants.TRADE_WITH_BANK_RATIO / 2); i++) {
            resourceWallet.addResource(ResourceType.GRAIN, 1);
        }
        testPlayer.setWallet(resourceWallet);
        testPlayer = playerService.save(testPlayer);

        // perform
        List<Move> moves = MoveCalculator.calculateTradeMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(0, moves.size(),
                "player cannot afford trade move");
    }

    @Test
    public void testCalculatePurchaseMoves_valid() {

        // player can afford purchase
        testPlayer.setWallet(new DevelopmentCard().getPrice());
        testPlayer = playerService.save(testPlayer);

        // perform
        List<PurchaseMove> moves = MoveCalculator.calculatePurchaseMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(1, moves.size(),
                "a move must be added");
        assertEquals(PurchaseMove.class, moves.get(0).getClass(),
                "the added move must be a purchase move");
    }

    @Test
    public void testCalculatePurchaseMoves_notEnoughResources() {

        // player can NOT afford purchase
        testPlayer.setWallet(new ResourceWallet());
        testPlayer = playerService.save(testPlayer);

        // perform
        List<PurchaseMove> moves = MoveCalculator.calculatePurchaseMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(0, moves.size(),
                "the player cannot afford this move");
    }

    // -- development card moves --

    @Test
    public void testCalculateAllKnightMoves_valid() {

        // perform
        List<Move> moves = MoveCalculator.calculateAllKnightMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(testBoard.getTiles().size(), moves.size(),
                "there must be one knight move for every tile on board");
        for (Move move : moves) {
            assertEquals(KnightMove.class, move.getClass(),
                    "the added move must be knight move");
        }
    }

    @Test
    public void testCalculateAllMonopolyMoves() {

        // no preconditions

        // perform
        List<Move> moves = MoveCalculator.calculateAllMonopolyMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(ResourceType.values().length, moves.size(),
                "there should be a separate move for every available resource");
        for (Move move : moves) {
            assertEquals(MonopolyMove.class, move.getClass(),
                    "the added move must be a monopoly move");
        }
    }

    @Test
    public void testCalculateAllPlentyMoves() {

        // no preconditions

        // perform
        List<Move> moves = MoveCalculator.calculateAllPlentyMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(Math.pow(ResourceType.values().length, 2), moves.size(),
                "there should be a separate move for every combination of two resource types");
        for (Move move : moves) {
            assertEquals(PlentyMove.class, move.getClass(),
                    "the added move must be a monopoly move");
        }
    }

    @Test
    public void testCalculateAllStealMoves() {

        // set robber on tile
        testBoard.getTiles().get(0).setHasRobber(true);

        // set opponent building on tile
        Player opponent = setupSecondTestPlayer();
        Coordinate coordinate = testBoard.getTiles().get(0).getCoordinates().get(0);
        City city = new City();
        city.setCoordinate(coordinate);
        city.setUserId(opponent.getUserId());

        testBoard.addCity(city);

        // save entities
        testPlayer = playerService.save(testPlayer);
        opponent = playerService.save(opponent);

        boardRepository.save(testBoard);
        gameRepository.save(testGame);

        // perform
        List<Move> moves = MoveCalculator.calculateAllStealMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(1, moves.size(),
                "a steal move must be added");
        assertEquals(StealMove.class, moves.get(0).getClass(),
                "the move must be a steal move");
        StealMove stealMove = (StealMove) moves.get(0);
        assertEquals(opponent.getUserId(), stealMove.getVictimId(),
                "the victim must be the player with building on robber tile");

    }

    @Test
    public void testCalculateAllRoadProgressMoves() {

    }

}
