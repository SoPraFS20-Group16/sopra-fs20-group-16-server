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

    private void setupTestMove(Move move, Player player, Game game) {
        move.setUserId(player.getUserId());
        move.setGameId(game.getId());
    }

    // -- start move(s) --

    @Test
    public void testCalculateStartMove() {

        // no additional preconditions

        // perform
        List<Move> moves = MoveCalculator.calculateStartMove(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(1, moves.size(),
                "one move must be added");
        assertEquals(StartMove.class, moves.get(0).getClass(),
                "the move must be a start move");
        StartMove startMove = (StartMove) moves.get(0);
        assertEquals(startMove.getUserId(), testGame.getCreatorId(),
                "the userId of the start move must be the creatorId");
    }

    // -- initial moves --

    @Test
    public void testCalculateFirstPassMove() {

        // no additional preconditions
        // perform
        List<Move> moves = MoveCalculator.calculateFirstPassMove(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(1, moves.size(),
                "one move must be added");
        assertEquals(FirstPassMove.class, moves.get(0).getClass(),
                "the move must be a firstPass move");
    }

    @Test
    public void testCalculateFirstSettlementMoves_emptyBoard() {

        // no additional preconditions

        // perform
        List<Move> moves = MoveCalculator.calculateFirstSettlementMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(testBoard.getAllCoordinates().size(), moves.size(),
                "one move must be added for every coordinate");
        for (Move move : moves) {
            assertEquals(FirstSettlementMove.class, move.getClass(),
                    "the move must be a firstSettlement move");
            assertEquals(Settlement.class, ((BuildMove) move).getBuilding().getClass(),
                    "the building must be a settlement");
        }
    }

    @Test
    public void testCalculateFirstSettlementMoves_nonEmptyBoard() {

        // set buildings on testBoard
        Coordinate coordinate = testBoard.getAllCoordinates().get(0);

        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate);
        settlement.setUserId(testPlayer.getUserId());

        testBoard.addSettlement(settlement);

        // perform
        List<Move> moves = MoveCalculator.calculateFirstSettlementMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        // one settlement reduces the available building coordinates by 4
        // if building is located at edge of board, then only by 3
        assertThat(moves.size(), anyOf(equalTo(testBoard.getAllCoordinates().size() - 3),
                equalTo(testBoard.getAllCoordinates().size() - 4)));
    }

    @Test
    public void testCalculateFirstRoadMoves_valid() {

        // set buildings on testBoard and create firstSettlementMove
        Coordinate coordinate = testBoard.getAllCoordinates().get(0);

        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate);
        settlement.setUserId(testPlayer.getUserId());

        FirstSettlementMove firstSettlementMove = new FirstSettlementMove();
        setupTestMove(firstSettlementMove, testPlayer, testGame);
        firstSettlementMove.setBuilding(settlement);

        testBoard.addSettlement(settlement);

        gameService.save(testGame);

        // perform

        List<Move> moves = MoveCalculator.calculateFirstRoadMoves(testGame, firstSettlementMove);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        // a settlement has three adjacent road building options
        // if building is located at edge of board, then only 2
        assertThat(moves.size(), anyOf(equalTo(2), equalTo(3)));
        for (Move move : moves) {
            assertEquals(FirstRoadMove.class, move.getClass(),
                    "the move must be a firstRoad move");
            assertEquals(Road.class, ((BuildMove) move).getBuilding().getClass(),
                    "the building must be road");
            assertTrue(((BuildMove) move).getBuilding().getCoordinates().contains(coordinate),
                    "every possible road should share a coordinate with the connecting settlement");
        }
    }

    // -- standard moves --

    // - default moves -

    @Test
    public void testCalculatePassMove() {

        // no additional preconditions
        // perform
        List<Move> moves = MoveCalculator.calculatePassMove(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(1, moves.size(),
                "one move must be added");
        assertEquals(PassMove.class, moves.get(0).getClass(),
                "the move must be a firstPass move");

    }

    @Test
    public void testCalculateDiceMove() {

        // no additional preconditions
        // perform
        List<Move> moves = MoveCalculator.calculateDiceMove(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(1, moves.size(),
                "one move must be added");
        assertEquals(DiceMove.class, moves.get(0).getClass(),
                "the move must be a dice move");

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

        // a settlement has three adjacent road building options
        // if building is located at edge of board, then only 2
        assertThat(moves.size(), anyOf(equalTo(2), equalTo(3)));
        for (BuildMove move : moves) {
            assertEquals(BuildMove.class, move.getClass(),
                    "the move must be a build move");
            assertEquals(Road.class, move.getBuilding().getClass(),
                    "the building should be a road");
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
        for (BuildMove move : moves) {
            assertEquals(BuildMove.class, move.getClass(),
                    "the move must be a build move");
            assertEquals(Road.class, move.getBuilding().getClass(),
                    "the building should be a road");
            assertTrue(move.getBuilding().getCoordinates().contains(coordinate),
                    "every possible road should share a coordinate with the connecting city");
        }
    }

    @Test
    public void testCalculateRoadMoves_connectingToRoad_valid() {

        // player can afford road
        testPlayer.setWallet(new Road().getPrice());
        playerService.save(testPlayer);

        // add settlement on board with one adjacent road
        Coordinate coordinate = testBoard.getTiles().get(0).getCoordinates().get(5);

        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate);
        settlement.setUserId(testPlayer.getUserId());

        testBoard.addSettlement(settlement);

        Road road = new Road();
        road.setCoordinate1(coordinate);
        road.setCoordinate2(coordinate.getNeighbors().get(0));
        road.setUserId(testPlayer.getUserId());

        testBoard.addRoad(road);

        // perform
        List<BuildMove> moves = MoveCalculator.calculateRoadMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        // a building has three adjacent road options, if not build at edge of board
        // a road already adjacent to building provides an extra road building option
        assertEquals(4, moves.size(),
                "a road provides one additional road building option");
        for (Move move : moves) {
            assertEquals(BuildMove.class, move.getClass(),
                    "the move must be a build move");
            assertEquals(Road.class, ((BuildMove) move).getBuilding().getClass(),
                    "the building of the move must be a road");
            // every possible road should shares a coordinate with either the settlement or the adjacent road
            assertTrue(((BuildMove) move).getBuilding().getCoordinates().contains(coordinate) ||
                    ((BuildMove) move).getBuilding().getCoordinates().contains(coordinate.getNeighbors().get(0)));
        }
    }

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

        // player can afford road
        testPlayer.setWallet(new Road().getPrice());
        testPlayer = playerService.save(testPlayer);

        // only opponent has building on board
        Player opponent = setupSecondTestPlayer();

        Coordinate coordinate = testBoard.getTiles().get(0).getCoordinates().get(0);
        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate);
        settlement.setUserId(opponent.getUserId());

        testBoard.addSettlement(settlement);

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

        // player can afford settlement
        testPlayer.setWallet(new Settlement().getPrice());
        testPlayer = playerService.save(testPlayer);

        // setup board, create settlement with two adjacent roads
        Coordinate coordinate = testBoard.getAllCoordinates().get(0);

        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate);
        settlement.setUserId(testPlayer.getUserId());

        testBoard.addSettlement(settlement);

        Road road1 = new Road();
        road1.setCoordinate1(coordinate);
        road1.setCoordinate2(coordinate.getNeighbors().get(0));
        road1.setUserId(testPlayer.getUserId());

        testBoard.addRoad(road1);

        Road road2 = new Road();
        road2.setCoordinate1(coordinate.getNeighbors().get(0));
        road2.setCoordinate2(coordinate.getNeighbors().get(0).getNeighbors().get(0));
        road2.setUserId(testPlayer.getUserId());

        testBoard.addRoad(road2);

        // perform
        List<BuildMove> moves = MoveCalculator.calculateSettlementMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(1, moves.size(),
                "for every open road end point, a move gets added");
        assertEquals(BuildMove.class, moves.get(0).getClass(),
                "the added move must be a build move");
        assertEquals(Settlement.class, moves.get(0).getBuilding().getClass(),
                "the building must be a settlement");
    }

    @Test
    public void testCalculateSettlementMoves_notEnoughResources() {

        // empty resource wallet
        testPlayer.setWallet(new ResourceWallet());
        testPlayer = playerService.save(testPlayer);

        // setup board, create settlement with one adjacent road
        Coordinate coordinate = testBoard.getAllCoordinates().get(0);

        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate);
        settlement.setUserId(testPlayer.getUserId());

        testBoard.addSettlement(settlement);

        Road road = new Road();
        road.setCoordinate1(coordinate);
        road.setCoordinate2(coordinate.getNeighbors().get(0));
        road.setUserId(testPlayer.getUserId());

        testBoard.addRoad(road);

        // perform
        List<BuildMove> moves = MoveCalculator.calculateSettlementMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(0, moves.size(),
                "the player cannot afford this move");

    }

    @Test
    public void testCalculateSettlementMoves_maxNumberOfSettlementsReached() {

        // player can afford settlement
        testPlayer.setWallet(new City().getPrice());
        testPlayer = playerService.save(testPlayer);

        // add road on board (provides valid building coordinate)
        Coordinate coordinate = testBoard.getAllCoordinates().get(0);

        Road road = new Road();
        road.setCoordinate1(coordinate);
        road.setCoordinate2(coordinate.getNeighbors().get(0));
        road.setUserId(testPlayer.getUserId());

        testBoard.addRoad(road);

        // player has already max amount of allowed cities build on board
        for (int i = 0; i < PlayerConstants.MAX_NUMBER_SETTLEMENTS; i++) {
            Settlement settlement = new Settlement();
            settlement.setUserId(testPlayer.getUserId());
            testBoard.addSettlement(settlement);
        }

        // perform
        List<BuildMove> moves = MoveCalculator.calculateSettlementMoves(testGame);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(0, moves.size(),
                "the player cannot build another settlement, since max amount is reached");

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

        // add city on board
        Coordinate coordinate = testBoard.getAllCoordinates().get(0);

        City city = new City();
        city.setCoordinate(coordinate);
        city.setUserId(testPlayer.getUserId());

        testBoard.addCity(city);

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
        for (Move move : moves) {
            assertEquals(TradeMove.class, move.getClass(),
                    "the added move must be a trade move");
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

        // no preconditions

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
    public void testCalculateAllMonopolyMoves_valid() {

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
    public void testCalculateAllPlentyMoves_valid() {

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
                    "the added move must be a plenty move");
        }
    }

    @Test
    public void testCalculateAllStealMoves_valid() {

        // set robber on tile
        testBoard.getTiles().get(0).setHasRobber(true);

        // set opponent buildings on tile
        Player opponent = setupSecondTestPlayer();

        Coordinate coordinate1 = testBoard.getTiles().get(0).getCoordinates().get(0);
        Coordinate coordinate2 = testBoard.getTiles().get(0).getCoordinates().get(2);

        City city = new City();
        city.setCoordinate(coordinate1);
        city.setUserId(opponent.getUserId());

        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate2);
        settlement.setUserId(opponent.getUserId());

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
                "a steal move must be added for every opponent");
        assertEquals(StealMove.class, moves.get(0).getClass(),
                "the move must be a steal move");
        StealMove stealMove = (StealMove) moves.get(0);
        assertEquals(opponent.getUserId(), stealMove.getVictimId(),
                "the victim must be the player with building on tile with robber");
    }

    @Test
    public void testCalculateAllStealMoves_noBuildingOnRobberTile() {

        // set robber on tile
        testBoard.getTiles().get(0).setHasRobber(true);

        // set opponent buildings on tile with NO robber
        Player opponent = setupSecondTestPlayer();

        Coordinate coordinate1 = testBoard.getTiles().get(11).getCoordinates().get(0);
        Coordinate coordinate2 = testBoard.getTiles().get(11).getCoordinates().get(2);

        City city = new City();
        city.setCoordinate(coordinate1);
        city.setUserId(opponent.getUserId());

        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate2);
        settlement.setUserId(opponent.getUserId());

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

        assertEquals(0, moves.size(),
                "when no buildings are on robber tile, there is no steal move");
    }

    @Test
    public void testCalculateAllRoadProgressMoves_maxNumberOfRoadsReached() {

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
        List<Move> moves = MoveCalculator.calculateAllRoadProgressMoves(testGame, 0);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        assertEquals(0, moves.size(),
                "the player cannot build another road, since max amount is reached");

    }

    @Test
    public void testCalculateAllRoadProgressMoves_valid() {

        // player can afford road
        testPlayer.setWallet(new Road().getPrice());
        playerService.save(testPlayer);

        // add settlement on board with one adjacent road
        Coordinate coordinate = testBoard.getTiles().get(0).getCoordinates().get(5);

        Settlement settlement = new Settlement();
        settlement.setCoordinate(coordinate);
        settlement.setUserId(testPlayer.getUserId());

        testBoard.addSettlement(settlement);

        Road road = new Road();
        road.setCoordinate1(coordinate);
        road.setCoordinate2(coordinate.getNeighbors().get(0));
        road.setUserId(testPlayer.getUserId());

        testBoard.addRoad(road);

        // perform
        List<Move> moves = MoveCalculator.calculateAllRoadProgressMoves(testGame, 0);
        moveRepository.saveAll(moves);

        // assert
        moveService.findMovesForGameAndPlayer(testGame.getId(), testPlayer.getUserId());

        // a building has three adjacent road options, if not build at edge of board
        // a road already adjacent to building provides an extra road building option
        assertEquals(4, moves.size(),
                "a road provides one additional road building option");
        for (Move move : moves) {
            assertEquals(RoadProgressMove.class, move.getClass(),
                    "the move must be a roadProgress move");
            assertEquals(Road.class, ((RoadProgressMove) move).getBuilding().getClass(),
                    "the building of the move must be a road");
            // every possible road should shares a coordinate with either the settlement or the adjacent road
            assertTrue(((RoadProgressMove) move).getBuilding().getCoordinates().contains(coordinate) ||
                    ((RoadProgressMove) move).getBuilding().getCoordinates().contains(coordinate.getNeighbors().get(0)));
        }
    }
}
