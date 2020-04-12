package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("coordinateRepository")
public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {

    Coordinate findByXAndY(int x, int y);
}
