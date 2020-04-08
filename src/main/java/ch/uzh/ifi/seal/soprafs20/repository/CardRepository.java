package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.constant.CardType;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("cardRepository")
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllByType(CardType cardType);
}
