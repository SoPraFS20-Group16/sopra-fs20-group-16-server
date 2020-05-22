package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.standard.TradeMoveHandler;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "TRADE_MOVE")
public class TradeMove extends Move {

    @Enumerated(EnumType.ORDINAL)
    private ResourceType neededType;

    @Enumerated(EnumType.ORDINAL)
    private ResourceType offeredType;


    @Override
    public MoveHandler getMoveHandler() {
        return new TradeMoveHandler();
    }

    public ResourceType getNeededType() {
        return neededType;
    }

    // getters and setters
    public void setNeededType(ResourceType neededType) {
        this.neededType = neededType;
    }

    public ResourceType getOfferedType() {
        return offeredType;
    }

    public void setOfferedType(ResourceType tradedType) {
        this.offeredType = tradedType;
    }
}
