package ch.uzh.ifi.seal.soprafs20.entity.moves.development;

import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.development.StealMoveHandler;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "STEAL_MOVE")
public class StealMove extends Move {

    @Column
    private Long victimId;

    @Override
    public MoveHandler getMoveHandler() {
        return new StealMoveHandler();
    }

    public Long getVictimId() {
        return victimId;
    }

    public void setVictimId(Long victimId) {
        this.victimId = victimId;
    }
}
