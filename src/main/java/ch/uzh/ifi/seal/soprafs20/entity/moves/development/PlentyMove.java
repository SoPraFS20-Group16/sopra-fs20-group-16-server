package ch.uzh.ifi.seal.soprafs20.entity.moves.development;

import ch.uzh.ifi.seal.soprafs20.constant.PlentyType;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.development.PlentyMoveHandler;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "PLENTY_MOVE")
public class PlentyMove extends Move {

    @Enumerated(EnumType.ORDINAL)
    private PlentyType plentyType;

    @Override
    public MoveHandler getMoveHandler() {
        return new PlentyMoveHandler();
    }

    public PlentyType getPlentyType() {
        return plentyType;
    }

    public void setPlentyType(PlentyType plentyType) {
        this.plentyType = plentyType;
    }
}
