package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("CoordinateRepository")
public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
}
