package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.repository.CoordinateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

public class CoordinateServiceTest {

    @Mock
    private CoordinateRepository coordinateRepository;

    @InjectMocks
    private CoordinateService coordinateService;

    private Coordinate coord1;
    private Coordinate coord2;
    private Coordinate coord3;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        coord1 = new Coordinate(1, 1);
        coord2 = new Coordinate(2, 2);
        coord3 = new Coordinate(3, 3);
    }

    @Test
    public void testFindNeighborsForCoordinate() {

        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(coord1);
        coordinates.add(coord2);
        coordinates.add(coord3);

        given(coordinateRepository.findAll()).willReturn(coordinates);
        given(coordinateRepository.findByXAndY(1, 1)).willReturn(coord1);
        given(coordinateRepository.findByXAndY(2, 2)).willReturn(coord2);
        given(coordinateRepository.findByXAndY(3, 3)).willReturn(coord3);

        coordinateService.calculateNeighbors();

        assertEquals(1, coord1.getNeighbors().size(), "coord1 should have one neighbor!");
        Coordinate coord1Neighbor = coord1.getNeighbors().get(0);

        assertEquals(coord2, coord1Neighbor, "coord2 should be coord1s neighbor!");
    }
}

