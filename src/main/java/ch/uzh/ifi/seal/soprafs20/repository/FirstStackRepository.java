package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.game.FirstStack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FirstStackRepository extends JpaRepository<FirstStack, Long> {

    FirstStack findByGameId(Long gameId);
}
