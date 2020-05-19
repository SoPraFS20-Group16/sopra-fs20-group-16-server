package ch.uzh.ifi.seal.soprafs20.service.integration;

import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.repository.CoordinateRepository;
import ch.uzh.ifi.seal.soprafs20.service.board.CoordinateService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;


@WebAppConfiguration
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class CoordinateServiceIntegrationTest {

    @Qualifier("coordinateRepository")
    @Autowired
    CoordinateRepository coordinateRepository;

    @Autowired
    CoordinateService coordinateService;


    private Coordinate coord1;
    private Coordinate coord2;
    private Coordinate coord3;
    private Coordinate coord4;
    private Coordinate coord5;
    private Coordinate coord6;
    private Coordinate coord7;

    private List<Coordinate> coordinates;


    @BeforeEach
    void setup() {

        //Ensure clean repo
        coordinateRepository.deleteAll();

        //setup
        coordinates = new ArrayList<>();

        coord1 = new Coordinate(0, 0);
        coord2 = new Coordinate(2, 0);
        coord3 = new Coordinate(1, 1);
        coord4 = new Coordinate(1, 2);
        coord5 = new Coordinate(0, 3);
        coord6 = new Coordinate(2, 3);
        coord7 = new Coordinate(0, 4);

        coordinates.add(coord1);
        coordinates.add(coord2);
        coordinates.add(coord3);
        coordinates.add(coord4);
        coordinates.add(coord5);
        coordinates.add(coord6);
        coordinates.add(coord7);

        coordinateRepository.saveAll(coordinates);
        coordinateRepository.flush();
    }

    @AfterEach
    void teardown() {
        coordinateRepository.deleteAll();
    }

    /**
     * Test if the neighbor calculations are persisted correctly
     * after calculation
     */
    @Test
    void testCalculateNeighbors() {

        coordinateService.calculateNeighbors();

        List<Coordinate> savedCoords = coordinateRepository.findAll();

        //Given the current setup every coordinate has between 1 and 3 neighbors
        for (Coordinate coord : savedCoords) {
            assertThat(coord.getNeighbors().size(),
                    anyOf(is(1), is(2), is(3)));
        }

        //Hint: Concrete implementation is tested as unit test
    }


}
