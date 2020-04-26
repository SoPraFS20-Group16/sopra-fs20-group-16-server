package ch.uzh.ifi.seal.soprafs20.entity.moves.first;

import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.initial.FirstSettlementMoveHandler;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FIRST_SETTLEMENT_MOVE")
public class FirstSettlementMove extends BuildMove {

    @Override
    public MoveHandler getMoveHandler() {
        return new FirstSettlementMoveHandler();
    }
}
