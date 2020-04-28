package ch.uzh.ifi.seal.soprafs20.service.integration;

import ch.uzh.ifi.seal.soprafs20.constant.PlayerConstants;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.PlayerQueue;
import ch.uzh.ifi.seal.soprafs20.entity.game.ResourceWallet;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.PassMove;
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
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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


}
