package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("gameRepository")
public interface GameRepository extends JpaRepository<Game, Long> {

    Game findByName(String name);
}
