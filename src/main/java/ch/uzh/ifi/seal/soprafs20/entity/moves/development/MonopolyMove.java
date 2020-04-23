package ch.uzh.ifi.seal.soprafs20.entity.moves.development;

import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.development.MonopolyMoveHandler;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "MONOPOLY_MOVE")
public class MonopolyMove extends Move {

    @Enumerated(EnumType.ORDINAL)
    private ResourceType monopolyType;

    @Override
    public MoveHandler getMoveHandler() {
        return new MonopolyMoveHandler();
    }

    public void setMonopolyType(ResourceType monopolyType) {
        this.monopolyType = monopolyType;
    }

    public ResourceType getMonopolyType() {
        return monopolyType;
    }
}
