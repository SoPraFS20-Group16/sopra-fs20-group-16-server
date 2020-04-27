package ch.uzh.ifi.seal.soprafs20.service.board;

import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.constant.TileType;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.repository.CoordinateRepository;
import ch.uzh.ifi.seal.soprafs20.repository.TileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TileService {

    private final TileRepository tileRepository;
    private final CoordinateRepository coordinateRepository;

    @Autowired
    public TileService(@Qualifier("tileRepository") TileRepository tileRepository,
                       @Qualifier("coordinateRepository") CoordinateRepository coordinateRepository) {
        this.tileRepository = tileRepository;
        this.coordinateRepository = coordinateRepository;
    }

    public Tile createTileWithTopCoordinate(Coordinate top) {

        Tile createdTile = new Tile();

        List<Coordinate> coordinates = new ArrayList<>();

        //Add top coordinate
        coordinates.add(top);

        //Create upper left coordinate
        Coordinate upperLeft = new Coordinate();
        upperLeft.setX(top.getX() - 1);
        upperLeft.setY(top.getY() + 1);
        coordinates.add(upperLeft);

        //Create upper right coordinate
        Coordinate upperRight = new Coordinate();
        upperRight.setX(top.getX() + 1);
        upperRight.setY(top.getY() + 1);
        coordinates.add(upperRight);

        //Create lower left coordinate
        Coordinate lowerLeft = new Coordinate();
        lowerLeft.setX(top.getX() - 1);
        lowerLeft.setY(top.getY() + 2);
        coordinates.add(lowerLeft);

        //Create lower right coordinate
        Coordinate lowerRight = new Coordinate();
        lowerRight.setX(top.getX() + 1);
        lowerRight.setY(top.getY() + 2);
        coordinates.add(lowerRight);

        //Create bottom coordinate
        Coordinate bottom = new Coordinate();
        bottom.setX(top.getX());
        bottom.setY(top.getY() + 3);
        coordinates.add(bottom);

        //Saved coordinates
        List<Coordinate> savedCoordinates = new ArrayList<>();

        for (Coordinate coordinate : coordinates) {

            Coordinate saved = coordinateRepository.save(coordinate);
            savedCoordinates.add(saved);

        }

        coordinateRepository.flush();

        //Add the coordinates to the tile
        createdTile.setCoordinates(savedCoordinates);


        return tileRepository.saveAndFlush(createdTile);
    }

    public ResourceType convertToResource(TileType tileType) {

        ResourceType resourceType;

        switch (tileType) {
            case HILL:
                resourceType = ResourceType.BRICK;
                break;
            case FIELD:
                resourceType = ResourceType.GRAIN;
                break;
            case FOREST:
                resourceType = ResourceType.LUMBER;
                break;
            case MOUNTAIN:
                resourceType = ResourceType.ORE;
                break;
            case PASTURE:
                resourceType = ResourceType.WOOL;
                break;
            default:
                throw new IllegalStateException("Unknown tile type or desert not allowed!");
        }

        return resourceType;

    }

    public void setRobber(Long tileId, Board board) {

        // remove robber from previous tile
        for (Tile tile : board.getTiles()) {
            if (tile.hasRobber()) {
                tile.setHasRobber(false);
                break;
            }
        }

        // add robber
        Tile tile = tileRepository.findById(tileId).orElseThrow();
        tile.setHasRobber(true);
    }
}
