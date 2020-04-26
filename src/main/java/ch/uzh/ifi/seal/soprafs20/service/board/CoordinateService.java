package ch.uzh.ifi.seal.soprafs20.service.board;

import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.repository.CoordinateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CoordinateService {

    private final CoordinateRepository coordinateRepository;

    @Autowired
    public CoordinateService(@Qualifier("coordinateRepository") CoordinateRepository coordinateRepository) {
        this.coordinateRepository = coordinateRepository;
    }

    public void calculateNeighbors() {

        List<Coordinate> coordinates = coordinateRepository.findAll();

        for (Coordinate coordinate : coordinates) {
            coordinate.setNeighbors(calculateNeighborsOf(coordinate));
        }

        coordinateRepository.saveAll(coordinates);
        coordinateRepository.flush();
    }

    private List<Coordinate> calculateNeighborsOf(Coordinate coordinate) {

        List<Coordinate> neighbors = new ArrayList<>();

        //It is possible to improve this search considerably if necessary!

        //Go over all eight possible neighbors
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {

                //If coordinate is self then skip
                if (i == 0 && j == 0) {
                    continue;
                }

                //Calculate neighbor coordinate
                int x = coordinate.getX() + i;
                int y = coordinate.getY() + j;

                //Look up the coordinate
                Coordinate found = coordinateRepository.findByXAndY(x, y);

                //If the coordinate is not null, add as a neighbor
                if (found != null) {
                    neighbors.add(found);
                }
            }
        }
        //Return the found neighbors
        return neighbors;
    }
}
