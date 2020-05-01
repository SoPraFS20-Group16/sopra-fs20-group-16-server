package ch.uzh.ifi.seal.soprafs20.service.unit;


import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.repository.CoordinateRepository;
import ch.uzh.ifi.seal.soprafs20.service.board.CoordinateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

public class CoordinateServiceTest {

    @Mock
    private CoordinateRepository coordinateRepository;

    @InjectMocks
    private CoordinateService coordinateService;

    private Coordinate coord1;
    private Coordinate coord2;
    private Coordinate coord3;
    private Coordinate coord4;
    private Coordinate coord5;
    private Coordinate coord6;
    private Coordinate coord7;

    private List<Coordinate> coordinates;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

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
    }

    /**
     * Tests the calculation of neighbors in a sample of the hexagon coordinate grid
     * The given grid covers a wider range of possible neighbor constellations.
     */
    @Test
    public void testFindNeighborsForCoordinate() {

        //Given
        given(coordinateRepository.findAll()).willReturn(coordinates);
        given(coordinateRepository.findByXAndY(0, 0)).willReturn(coord1);
        given(coordinateRepository.findByXAndY(2, 0)).willReturn(coord2);
        given(coordinateRepository.findByXAndY(1, 1)).willReturn(coord3);
        given(coordinateRepository.findByXAndY(1, 2)).willReturn(coord4);
        given(coordinateRepository.findByXAndY(0, 3)).willReturn(coord5);
        given(coordinateRepository.findByXAndY(2, 3)).willReturn(coord6);
        given(coordinateRepository.findByXAndY(0, 4)).willReturn(coord7);


        //Make the calculations
        coordinateService.calculateNeighbors();

        //Check the resulting neighbor relationships

        //Check coord1
        assertEquals(1, coord1.getNeighbors().size(), "coord1 should have one neighbor!");
        Coordinate coord1Neighbor = coord1.getNeighbors().get(0);
        assertEquals(coord3, coord1Neighbor, "coord3 should be coord1s neighbor!");


        //Check coord3
        assertEquals(3, coord3.getNeighbors().size(), "coord3 should have 3 neighbors");
        assertTrue(coord3.getNeighbors().contains(coord1), "coord1 is coord3s neighbor");
        assertTrue(coord3.getNeighbors().contains(coord2), "coord2 is coord3s neighbor");
        assertTrue(coord3.getNeighbors().contains(coord4), "coord4 is coord3s neighbor");

        assertEquals(3, coord4.getNeighbors().size(), "coord4 should have 3 neighbors");
        assertTrue(coord4.getNeighbors().contains(coord3), "coord1 is coord4s neighbor");
        assertTrue(coord4.getNeighbors().contains(coord5), "coord2 is coord4s neighbor");
        assertTrue(coord4.getNeighbors().contains(coord6), "coord4 is coord4s neighbor");

        //Check coord5
        assertEquals(2, coord5.getNeighbors().size(), "coord5 should have 2 neighbors");
        assertTrue(coord5.getNeighbors().contains(coord4), "coord1 is coord5s neighbor");
        assertTrue(coord5.getNeighbors().contains(coord7), "coord2 is coord5s neighbor");
    }
}

