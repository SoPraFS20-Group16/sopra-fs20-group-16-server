package ch.uzh.ifi.seal.soprafs20.service.unit;

import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.repository.CoordinateRepository;
import ch.uzh.ifi.seal.soprafs20.repository.TileRepository;
import ch.uzh.ifi.seal.soprafs20.service.board.TileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TileServiceTest {

    @Mock
    private TileRepository tileRepository;

    @InjectMocks
    private TileService tileService;

    @Mock
    private CoordinateRepository coordinateRepository;

    //TestObjects
    private Coordinate testCoordinate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testCoordinate = new Coordinate();
        testCoordinate.setX(1);
        testCoordinate.setY(0);
    }

    @Test
    public void testCreateTileWithTopCoordinate() {
        when(tileRepository.saveAndFlush(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
        when(coordinateRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        Tile created = tileService.createTileWithTopCoordinate(testCoordinate);

        assertNotNull(created, "The created tile should not be null!");
        assertNotNull(created.getCoordinates(), "There should be a list of coordinates!");
        assertFalse(created.getCoordinates().isEmpty(), "The coordinate array should not be empty!");

        List<Coordinate> coordinates = created.getCoordinates();

        //Assert the top coordinate exists
        assertEquals(6, coordinates.size(), "There should be 6 coordinates!");
        assertTrue(created.hasCoordinate(testCoordinate), "The TestCoordinate should be in the list!");

        //Assert the upper left coordinate exists
        Coordinate upperLeft = new Coordinate();
        upperLeft.setX(0);
        upperLeft.setY(1);
        assertTrue(created.hasCoordinate(upperLeft), "UpperLeft should be in the list!!");

        //Assert the  upper right coordinate exists
        Coordinate upperRight = new Coordinate();
        upperRight.setX(2);
        upperRight.setY(1);
        assertTrue(created.hasCoordinate(upperRight));

        //Assert the lower left coordinate exists
        Coordinate lowerLeft = new Coordinate();
        lowerLeft.setX(0);
        lowerLeft.setY(2);
        assertTrue(created.hasCoordinate(lowerLeft));

        //Assert the lower right coordinate exists
        Coordinate lowerRight = new Coordinate();
        lowerRight.setX(2);
        lowerRight.setY(2);
        assertTrue(created.hasCoordinate(lowerRight));

        //Assert bottom coordinate exists
        Coordinate bottom = new Coordinate();
        bottom.setX(1);
        bottom.setY(3);
        assertTrue(created.hasCoordinate(bottom));
    }
}

