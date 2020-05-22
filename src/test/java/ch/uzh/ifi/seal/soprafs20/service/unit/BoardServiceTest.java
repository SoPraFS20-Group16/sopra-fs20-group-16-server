package ch.uzh.ifi.seal.soprafs20.service.unit;

import ch.uzh.ifi.seal.soprafs20.constant.BoardConstants;
import ch.uzh.ifi.seal.soprafs20.constant.TileType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.CoordinateRepository;
import ch.uzh.ifi.seal.soprafs20.repository.TileRepository;
import ch.uzh.ifi.seal.soprafs20.service.board.BoardService;
import ch.uzh.ifi.seal.soprafs20.service.board.CoordinateService;
import ch.uzh.ifi.seal.soprafs20.service.board.TileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BoardServiceTest {

    private final Long testGameId = 123L;
    @InjectMocks
    TileService tileService;
    @Mock
    private BoardRepository boardRepository;
    @InjectMocks
    private BoardService boardService;
    @Mock
    private TileRepository tileRepository;
    @Mock
    private CoordinateService coordinateService;
    @Mock
    private CoordinateRepository coordinateRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(boardService, "coordinateService", coordinateService);
        ReflectionTestUtils.setField(tileService, "coordinateRepository", coordinateRepository);
        ReflectionTestUtils.setField(tileService, "tileRepository", tileRepository);
        ReflectionTestUtils.setField(boardService, "tileService", tileService);

    }

    @Test
    void testCreateBoard() {
        when(boardRepository.saveAndFlush(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
        when(tileRepository.saveAndFlush(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
        when(coordinateRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        Board createdBoard = boardService.createBoard(testGameId);

        List<Tile> tiles = createdBoard.getTiles();

        assertEquals(19, tiles.size(), "There should be 19 Tiles");

        int deserts = 0;
        for (Tile tile : tiles) {
            if (tile.getType().equals(TileType.DESERT)) {
                deserts++;
            }
        }
        assertEquals(BoardConstants.NUMBER_OF_DESERTS, deserts, "Not the right number of deserts");

        int forests = 0;
        for (Tile tile : tiles) {
            if (tile.getType().equals(TileType.FOREST)) {
                forests++;
            }
        }
        assertEquals(BoardConstants.NUMBER_OF_FORESTS, forests, "Not the right number of forests");

    }

    @Test
    void testGetBuildingsForTileForPlayer() {
        when(boardRepository.saveAndFlush(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
        when(tileRepository.saveAndFlush(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
        when(coordinateRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        //Setup
        Tile tile = tileService.createTile(new Coordinate(1, 0), testGameId);

        Long userId = 112233L;
        Long enemyId = 332211L;

        Board board = new Board();
        board.setTiles(Collections.singletonList(tile));

        Game game = new Game();
        game.setBoard(board);

        Player player = new Player();
        player.setUserId(userId);

        //Player buildings
        Settlement settlement = new Settlement();
        settlement.setCoordinate(tile.getCoordinates().get(1));
        settlement.setUserId(userId);

        City city = new City();
        city.setCoordinate(tile.getCoordinates().get(2));
        city.setUserId(userId);

        board.addSettlement(settlement);
        board.addCity(city);

        //Random Enemy Buildings
        Settlement enemySettlement = new Settlement();
        enemySettlement.setCoordinate(tile.getCoordinates().get(3));
        enemySettlement.setUserId(enemyId);

        City enemyCity = new City();
        enemyCity.setCoordinate(tile.getCoordinates().get(4));
        enemyCity.setUserId(enemyId);

        board.addSettlement(enemySettlement);
        board.addCity(enemyCity);


        //Call the method
        List<Building> buildings = boardService.getBuildingsFromTileForPlayer(game, tile, player);

        assertEquals(2, buildings.size(), "There should be two buildings");

        for (Building building : buildings) {
            assertEquals(userId, building.getUserId(), "The buildings should belong to the player");
        }
    }
}
