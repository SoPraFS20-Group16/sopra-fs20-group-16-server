package ch.uzh.ifi.seal.soprafs20.service.integration;

import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.service.TileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebAppConfiguration
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
public class TileServiceIntegrationTest {

    @Autowired
    TileService tileService;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void setup() {
        entityManager.clear();
    }

    @AfterEach
    public void teardown() {

        entityManager.clear();
    }

    @Test
    public void testCreateTile() {

        Coordinate topCoordinate = new Coordinate(1, 0);

        Tile created = tileService.createTileWithTopCoordinate(topCoordinate);

        assertNotNull(created, "The created tile should not be null!");
        assertEquals(6, created.getCoordinates().size(), "There should be 6 coordinates in the array!");

        //Add another tile
        Tile another = tileService.createTileWithTopCoordinate(new Coordinate(1, 3));

        assertNotNull(another, "The created tile should not be null!");
        assertEquals(6, another.getCoordinates().size(), "There should be 6 coordinates in the array!");

    }

}
