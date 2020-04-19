package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.PurchaseMoveHandler;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PURCHASE_MOVE")
public class PurchaseMove extends Move {

    @OneToOne
    DevelopmentCard developmentCard;

    @Override
    public MoveHandler getMoveHandler() {
        return new PurchaseMoveHandler();
    }

    public DevelopmentCard getDevelopmentCard() {
        return developmentCard;
    }

    public void setDevelopmentCard(DevelopmentCard developmentCard) {
        this.developmentCard = developmentCard;
    }
}
