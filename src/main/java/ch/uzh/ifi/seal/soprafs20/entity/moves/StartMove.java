package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.StartMoveHandler;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "START_MOVE")
public class StartMove extends Move {
    @Override
    public MoveHandler getMoveHandler() {
        return new StartMoveHandler();
    }
}
