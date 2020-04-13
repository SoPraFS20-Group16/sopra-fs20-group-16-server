package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.repository.CoordinateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebAppConfiguration
@SpringBootTest
public class TileServiceIntegrationTest {

    @Autowired
    TileService tileService;
    @Qualifier("coordinateRepository")
    @Autowired
    private CoordinateRepository coordinateRepository;

    @BeforeEach
    public void setup() {
        coordinateRepository.deleteAll();
    }

    @Test
    public void testCreateTile() {

        Coordinate topCoordinate = new Coordinate(1, 0);

        Tile created = tileService.createTileWithTopCoordinate(topCoordinate);

        assertNotNull(created, "The created tile should not be null!");
        assertEquals(6, created.getCoordinates().size(), "There should be 6 coordinates in the array!");

        //Test if the coordinates are saved in the coordinateRepository
        List<Coordinate> savedCoordinates = coordinateRepository.findAll();
        assertEquals(6, savedCoordinates.size(), "There should be 6 coordinates in the repository!");

        //Add another tile
        tileService.createTileWithTopCoordinate(new Coordinate(1, 3));

        savedCoordinates = coordinateRepository.findAll();

        assertEquals(11, savedCoordinates.size(),
                "The bottom of the first tile and the top of the second tile should be the same coordinate");
    }

}
