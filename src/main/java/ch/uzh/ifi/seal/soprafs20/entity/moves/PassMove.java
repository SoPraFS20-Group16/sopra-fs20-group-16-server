package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.standard.PassMoveHandler;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PASS_MOVE")
public class PassMove extends Move {

    @Override
    public MoveHandler getMoveHandler() {
        return new PassMoveHandler();
    }
}
