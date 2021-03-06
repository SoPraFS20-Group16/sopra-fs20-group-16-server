package ch.uzh.ifi.seal.soprafs20.entity.moves.initial;

import ch.uzh.ifi.seal.soprafs20.entity.moves.PassMove;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.initial.FirstPassMoveHandler;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FIRST_PASS_MOVE")
public class FirstPassMove extends PassMove {

    @Override
    public MoveHandler getMoveHandler() {
        return new FirstPassMoveHandler();
    }
}
