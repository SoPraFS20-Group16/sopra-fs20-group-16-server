package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.game.PlayerQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("queueRepository")
public interface QueueRepository extends JpaRepository<PlayerQueue, Long> {

    PlayerQueue findByGameId(Long gameId);
}
