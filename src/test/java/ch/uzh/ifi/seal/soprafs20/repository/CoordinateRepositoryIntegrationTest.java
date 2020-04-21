package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class CoordinateRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("coordinateRepository")
    @Autowired
    private CoordinateRepository coordinateRepository;

    //Test objects
    private Coordinate testCoordinate;

    @BeforeEach
    public void setup() {
        testCoordinate = new Coordinate();
        testCoordinate.setX(1);
        testCoordinate.setY(1);
    }

    @AfterEach
    public void teardown() {
        coordinateRepository.deleteAll();
    }

    /**
     * Since the primary key are the coordinates x and y,
     * there should not be multiple instances of the same coordinate
     */
    @Test
    public void testSaveNoDuplicates() {


        Coordinate duplicate = new Coordinate();
        duplicate.setX(testCoordinate.getX());
        duplicate.setY(testCoordinate.getY());

        Coordinate fakeNeighbor = new Coordinate();
        fakeNeighbor.setX(0);
        fakeNeighbor.setY(0);

        List<Coordinate> fakeNeighbors = Collections.singletonList(fakeNeighbor);

        duplicate.setNeighbors(fakeNeighbors);

        //Saving the two coordinates
        entityManager.persist(testCoordinate);
        entityManager.persist(fakeNeighbor);
        entityManager.flush();

        //Saving the duplicate updates the testCoordinate instead of creating a new one
        coordinateRepository.save(duplicate);
        coordinateRepository.flush();

        List<Coordinate> allCoordinates = coordinateRepository.findAll();

        //Only the testCoordinate and the fakeNeighbor are in the repository
        assertEquals(2, allCoordinates.size(), "There should only be two coordinates!");

        //Test if the retrieved neighbor equals the fake neighbor
        Coordinate retrievedNeighbor = coordinateRepository.findByXAndY(1, 1).getNeighbors().get(0);
        assertEquals(fakeNeighbor, retrievedNeighbor, "The retrievedNeighbor does not match the fakeNeighbor!");

    }
}
