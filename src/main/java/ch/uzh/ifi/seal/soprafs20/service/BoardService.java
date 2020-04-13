package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.BoardConstants;
import ch.uzh.ifi.seal.soprafs20.constant.TileType;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final TileService tileService;

    @Autowired
    public BoardService(@Qualifier("boardRepository") BoardRepository boardRepository,
                        TileService tileService) {

        this.boardRepository = boardRepository;
        this.tileService = tileService;
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

        //AddTiles

        //The new tile
        Tile newTile = tileService.createTileWithTopCoordinate(new Coordinate(3, 0);
        newTile.setTileNumber(6);


        return boardRepository.saveAndFlush(board);
    }

    /**
     * Returns with the correct number of TileType objects per Tile that should have that type
     *
     * @return the list of TileType Objects
     */
    private List<TileType> getTileTypeList() {

        List<TileType> typeList = new ArrayList<>();

        //Number of deserts
        for (int i = 0; i < BoardConstants.NUMBER_OF_DESERTS; i++) {
            typeList.add(TileType.DESERT);
        }

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
}
