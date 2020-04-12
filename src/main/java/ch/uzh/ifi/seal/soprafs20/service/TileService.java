package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
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

    @Autowired
    public TileService(@Qualifier("tileRepository") TileRepository tileRepository) {
        this.tileRepository = tileRepository;
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

        //Add the coordinates to the tile
        createdTile.setCoordinates(coordinates);

        return tileRepository.saveAndFlush(createdTile);
    }

}
