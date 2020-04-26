package ch.uzh.ifi.seal.soprafs20.service.board;

import ch.uzh.ifi.seal.soprafs20.constant.BoardConstants;
import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.constant.TileType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.RoadProgressMove;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final GameRepository gameRepository;

    private CoordinateService coordinateService;
    private TileService tileService;

    @Autowired
    public BoardService(@Qualifier("boardRepository") BoardRepository boardRepository,
                        @Qualifier("gameRepository") GameRepository gameRepository) {

        this.boardRepository = boardRepository;
        this.gameRepository = gameRepository;
    }

    @Autowired
    public void setTileService(TileService tileService) {
        this.tileService = tileService;
    }

    @Autowired
    public void setCoordinateService(CoordinateService coordinateService) {
        this.coordinateService = coordinateService;
    }


    /**
     * Creates a new board, saves it to the database and returns it
     *
     * @return the board
     */
    public Board createBoard() {

        //The empty board
        Board board = new Board();

        //The list of tiles
        List<Tile> tiles = new ArrayList<>();

        //A list of TileTypes that should be on the board in the correct number
        List<TileType> necessaryTiles = getTileTypeList();

        //The new tiles
        Tile newTile = tileService.createTileWithTopCoordinate(new Coordinate(3, 0));
        newTile.setTileNumber(6);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(5, 0));
        newTile.setTileNumber(3);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(7, 0));
        newTile.setTileNumber(8);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(2, 2));
        newTile.setTileNumber(2);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(4, 2));
        newTile.setTileNumber(4);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(6, 2));
        newTile.setTileNumber(5);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(8, 2));
        newTile.setTileNumber(10);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(1, 4));
        newTile.setTileNumber(5);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(3, 4));
        newTile.setTileNumber(9);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        //The desert tile
        newTile = tileService.createTileWithTopCoordinate(new Coordinate(5, 4));
        newTile.setTileNumber(0);
        newTile.setType(TileType.DESERT);
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(7, 4));
        newTile.setTileNumber(11);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(9, 4));
        newTile.setTileNumber(6);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(2, 6));
        newTile.setTileNumber(9);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(4, 6));
        newTile.setTileNumber(10);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(6, 6));
        newTile.setTileNumber(3);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(8, 6));
        newTile.setTileNumber(12);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(3, 8));
        newTile.setTileNumber(8);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(5, 8));
        newTile.setTileNumber(4);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        newTile = tileService.createTileWithTopCoordinate(new Coordinate(7, 8));
        newTile.setTileNumber(11);
        newTile.setType(necessaryTiles.remove(0));
        tiles.add(newTile);

        board.setTiles(tiles);

        //Calculate coordinate neighbors
        coordinateService.calculateNeighbors();
        return boardRepository.saveAndFlush(board);
    }

    /**
     * Returns with the correct number of TileType objects per Tile that should have that type
     *
     * @return the list of TileType Objects
     */
    private List<TileType> getTileTypeList() {

        List<TileType> typeList = new ArrayList<>();


        //Number of fields
        for (int i = 0; i < BoardConstants.NUMBER_OF_FIELDS; i++) {
            typeList.add(TileType.FIELD);
        }

        //Number of forests
        for (int i = 0; i < BoardConstants.NUMBER_OF_FORESTS; i++) {
            typeList.add(TileType.FOREST);
        }

        //Number of mountains
        for (int i = 0; i < BoardConstants.NUMBER_OF_MOUNTAINS; i++) {
            typeList.add(TileType.MOUNTAIN);
        }

        //Number of hills
        for (int i = 0; i < BoardConstants.NUMBER_OF_HILLS; i++) {
            typeList.add(TileType.HILL);
        }

        //Number of pastures
        for (int i = 0; i < BoardConstants.NUMBER_OF_PASTURES; i++) {
            typeList.add(TileType.PASTURE);
        }

        return typeList;
    }

    public void build(BuildMove move) {
        buildWorker(move.getBuilding(), move.getGameId(), move.getUserId());
    }

    public void build(RoadProgressMove move) {
        buildWorker(move.getRoad(), move.getGameId(), move.getUserId());
    }

    private void buildWorker(Building building, Long gameId, Long userId) {

        //Get the board on which the building is built
        Board board = getBoardByGameId(gameId);

        //Add owner information
        building.setUserId(userId);

        switch (building.getType()) {
            case SETTLEMENT:
                board.addSettlement((Settlement) building);
                break;
            case ROAD:
                board.addRoad((Road) building);
                break;
            case CITY:
                board.addCity((City) building);
                break;
            default:
                throw new IllegalStateException("Unknown building type not allowed!");
        }
    }

    public void removeSettlementForCity(BuildMove buildMove) {

        // get corresponding board
        Board board = getBoardByGameId(buildMove.getGameId());

        // get city coordinate
        if (buildMove.getBuilding().getClass() != City.class) {
            throw new IllegalStateException(ErrorMsg.REMOVE_SETTLEMENT_ERROR);
        }
        City city = (City) buildMove.getBuilding();

        // get settlement to be removed
        Settlement settlementRemove = null;
        for (Settlement settlement : board.getSettlements()) {
            if (settlement.getCoordinate() == city.getCoordinate()) {
                settlementRemove = settlement;
            }
        }

        // remove settlement
        board.getSettlements().remove(settlementRemove);
    }

    public List<Tile> getTilesWithNumber(Long gameId, int number) {

        //Get the board on which the building is built
        Board board = getBoardByGameId(gameId);

        // get tile(s) with corresponding number
        List<Tile> tiles = new ArrayList<>();

        for (Tile tile : board.getTiles()) {
            if (tile.getTileNumber() == number) {
                tiles.add(tile);
            }
        }

        // return tile(s)
        return tiles;
    }

    public List<Long> getPlayerIDsWithSettlement(Long gameId, Tile tile) {

        //Get the board on which the building is built
        Board board = getBoardByGameId(gameId);

        // get all valid coordinates
        List<Coordinate> coordinates = tile.getCoordinates();

        // get all IDs of settlement owners
        List<Long> playerIDs = new ArrayList<>();

        for (Settlement settlement: board.getSettlements()) {
            if (coordinates.contains(settlement.getCoordinate())) {
                playerIDs.add(settlement.getUserId());
            }
        }

        return playerIDs;
    }

    public List<Long> getPlayerIDsWithCity(Long gameId, Tile tile) {

        //Get the board on which the building is built
        Board board = getBoardByGameId(gameId);

        // get all valid coordinates
        List<Coordinate> coordinates = tile.getCoordinates();

        // get all IDs of settlement owners
        List<Long> playerIDs = new ArrayList<>();

        for (City city : board.getCities()) {
            if (coordinates.contains(city.getCoordinate())) {
                playerIDs.add(city.getUserId());
            }
        }

        return playerIDs;
    }

    private Board getBoardByGameId(Long gameId) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);

        if (gameOptional.isEmpty()) {
            throw new NullPointerException(ErrorMsg.NO_MOVE_FOR_NONEXISTING_GAME);
        }

        return gameOptional.get().getBoard();
    }

    public int getPointsFromBuildings(Game game, Player player) {

        int buildingPoins = 0;

        //Get the board on which the building is built
        Optional<Game> gameOptional = gameRepository.findById(game.getId());

        if (gameOptional.isEmpty()) {
            throw new NullPointerException(ErrorMsg.NO_MOVE_FOR_NONEXISTING_GAME);
        }

        Board board = gameOptional.get().getBoard();

        for (City city: board.getCities()) {
            if (city.getUserId().equals(player.getUserId())) {
                buildingPoins += city.getVictoryPoints();
            }
        }

        for (Settlement settlement: board.getSettlements()) {
            if (settlement.getUserId().equals(player.getUserId())) {
                buildingPoins += settlement.getVictoryPoints();
            }
        }

        return buildingPoins;
    }

}
