package ch.uzh.ifi.seal.soprafs20.entity.moves.development;

import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
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
    ResourceType plentyType1;

    @Enumerated(EnumType.ORDINAL)
    ResourceType plentyType2;

    @Override
    public MoveHandler getMoveHandler() {
        return new PlentyMoveHandler();
    }

    public ResourceType getPlentyType1() {
        return plentyType1;
    }

    public void setPlentyType1(ResourceType plentyType1) {
        this.plentyType1 = plentyType1;
    }

    public ResourceType getPlentyType2() {
        return plentyType2;
    }

    public void setPlentyType2(ResourceType plentyType2) {
        this.plentyType2 = plentyType2;
    }
}
