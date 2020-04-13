package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.CardMoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CARD_MOVE")
public class CardMove extends Move {

    @OneToOne
    private DevelopmentCard developmentCard;

    @Override
    public MoveHandler getMoveHandler() {
        return new CardMoveHandler();
    }

    public DevelopmentCard getDevelopmentCard() {
        return developmentCard;
    }

    public void setDevelopmentCard(DevelopmentCard developmentCard) {
        this.developmentCard = developmentCard;
    }
}
