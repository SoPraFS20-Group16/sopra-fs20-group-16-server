package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TileTest {

    private Tile testTile;
    private Coordinate testCoordinate;

    @BeforeEach
    void setup() {

        testTile = new Tile();

        testCoordinate = new Coordinate();
        testCoordinate.setX(1);
        testCoordinate.setY(1);
    }

    @Test
    void testHasCoordinate_isTrue() {

        List<Coordinate> coordinateList = Collections.singletonList(testCoordinate);

        testTile.setCoordinates(coordinateList);

        Coordinate expectedCoordinate = new Coordinate();
        expectedCoordinate.setX(1);
        expectedCoordinate.setY(1);

        assertTrue(testTile.hasCoordinate(expectedCoordinate));
    }

    @Test
    void testHasCoordinate_isFalse() {

        List<Coordinate> coordinateList = Collections.singletonList(testCoordinate);

        testTile.setCoordinates(coordinateList);

        Coordinate expectedCoordinate = new Coordinate();
        expectedCoordinate.setX(1);
        expectedCoordinate.setY(2);

        assertFalse(testTile.hasCoordinate(expectedCoordinate));
    }
}
