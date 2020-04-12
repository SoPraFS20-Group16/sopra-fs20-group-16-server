package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.repository.CoordinateRepository;
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

    @Test
    public void testCreateTile() {

        Coordinate topCoordinate = new Coordinate(1, 0);

        Tile created = tileService.createTileWithTopCoordinate(topCoordinate);

        assertNotNull(created, "The created tile should not be null!");
        assertEquals(6, created.getCoordinates().size(), "There should be 6 coordinates in the array!");

        //Test if the coordinates are saved in the coordinateRepository
        List<Coordinate> savedCoordinates = coordinateRepository.findAll();
        assertEquals(6, savedCoordinates.size(), "There should be 6 coordinates in the repository!");
    }

}
