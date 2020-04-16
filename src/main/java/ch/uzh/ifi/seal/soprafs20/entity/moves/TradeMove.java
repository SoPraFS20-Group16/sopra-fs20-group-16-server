package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.TradeMoveHandler;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TRADE_MOVE")
public class TradeMove extends Move {

    @OneToOne
    private DevelopmentCard developmentCard;

    @OneToOne
    private ResourceType neededType;

    @OneToOne
    private ResourceType tradedType;


    @Override
    public MoveHandler getMoveHandler() {
        return new TradeMoveHandler();
    }

    // getters and setters
    public void setDevelopmentCard(DevelopmentCard developmentCard) {
        this.developmentCard = developmentCard;
    }

    public DevelopmentCard getDevelopmentCard() {
        return developmentCard;
    }

    public void setNeededType(ResourceType neededType) {
        this.neededType = neededType;
    }

    public ResourceType getNeededType() {
        return neededType;
    }

    public void setTradedType(ResourceType tradedType) {
        this.tradedType = tradedType;
    }

    public ResourceType getTradedType() {
        return tradedType;
    }
}
