package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.BoardConstants;
import ch.uzh.ifi.seal.soprafs20.constant.TileType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.DiceMove;
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
    private final TileService tileService;
    private final GameRepository gameRepository;
    private final CoordinateService coordinateService;

    @Autowired
    public BoardService(@Qualifier("boardRepository") BoardRepository boardRepository,
                        @Qualifier("gameRepository") GameRepository gameRepository,
                        TileService tileService,
                        CoordinateService coordinateService) {

        this.boardRepository = boardRepository;
        this.tileService = tileService;
        this.gameRepository = gameRepository;
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

        //Get the building from the move
        Building building = move.getBuilding();

        //Get the board on which the building is built
        Optional<Game> gameOptional = gameRepository.findById(move.getGameId());

        if (gameOptional.isEmpty()) {
            throw new NullPointerException("There should not be a move for a nonexistent game!");
        }

        Board board = gameOptional.get().getBoard();

        //Add owner information
        building.setUserId(move.getUserId());

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

    public List<Tile> getTiles(DiceMove diceMove, int diceRoll) {

        //Get the board on which the building is built
        Optional<Game> gameOptional = gameRepository.findById(diceMove.getGameId());

        if (gameOptional.isEmpty()) {
            throw new NullPointerException("There should not be a move for a nonexistent game!");
        }

        Board board = gameOptional.get().getBoard();

        // get tiles with corresponding number
        List<Tile> tiles = new ArrayList<>();

        for (Tile tile: board.getTiles()) {
            if (tile.getTileNumber() == diceRoll) {
                tiles.add(tile);
            }
        }

        // return tiles
        return tiles;
    }

    public List<Building> getBuildingsOnTile(DiceMove diceMove, Tile tile) {

        //Get the board on which the building is built
        Optional<Game> gameOptional = gameRepository.findById(diceMove.getGameId());

        if (gameOptional.isEmpty()) {
            throw new NullPointerException("There should not be a move for a nonexistent game!");
        }

        Board board = gameOptional.get().getBoard();

        // get all valid coordinates
        List<Coordinate> coordinates = tile.getCoordinates();

        // get all buildings on corresponding tile
        List<Building> buildings = new ArrayList<>();

        for (Settlement settlement: board.getSettlements()) {
            if (coordinates.contains(settlement.getCoordinate())) {
                buildings.add(settlement);
            }
        }

        for (City city: board.getCities()) {
            if (coordinates.contains(city.getCoordinate())) {
                buildings.add(city);
            }
        }

        return buildings;
    }

    public List<Long> getPlayerIDsWithBuilding(DiceMove diceMove, List<Building> buildings) {

        //Get the board on which the building is built
        Optional<Game> gameOptional = gameRepository.findById(diceMove.getGameId());

        if (gameOptional.isEmpty()) {
            throw new NullPointerException("There should not be a move for a nonexistent game!");
        }

        Board board = gameOptional.get().getBoard();

        // get players with buildings
        List<Long> playerIDs = new ArrayList<>();

        for (Building building: buildings) {
            playerIDs.add(building.getUserId());
        }

        return playerIDs;
    }

    public List<Long> getPlayerIDsWithSettlement(DiceMove diceMove, Tile tile) {

        //Get the board on which the building is built
        Optional<Game> gameOptional = gameRepository.findById(diceMove.getGameId());

        if (gameOptional.isEmpty()) {
            throw new NullPointerException("There should not be a move for a nonexistent game!");
        }

        Board board = gameOptional.get().getBoard();

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

    public List<Long> getPlayerIDsWithCity(DiceMove diceMove, Tile tile) {

        //Get the board on which the building is built
        Optional<Game> gameOptional = gameRepository.findById(diceMove.getGameId());

        if (gameOptional.isEmpty()) {
            throw new NullPointerException("There should not be a move for a nonexistent game!");
        }

        Board board = gameOptional.get().getBoard();

        // get all valid coordinates
        List<Coordinate> coordinates = tile.getCoordinates();

        // get all IDs of settlement owners
        List<Long> playerIDs = new ArrayList<>();

        for (City city: board.getCities()) {
            if (coordinates.contains(city.getCoordinate())) {
                playerIDs.add(city.getUserId());
            }
        }

        return playerIDs;
    }
}
