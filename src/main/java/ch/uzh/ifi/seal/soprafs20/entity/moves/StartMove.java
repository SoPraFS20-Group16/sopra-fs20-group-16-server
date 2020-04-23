package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.StartMoveHandler;

public class StartMove extends Move {
    @Override
    public MoveHandler getMoveHandler() {
        return new StartMoveHandler();
    }
}
