package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.summary.GameSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("gameSummaryRepository")
public interface GameSummaryRepository extends JpaRepository<GameSummary, Long> {

    GameSummary findByGameId(Long gameId);
}
